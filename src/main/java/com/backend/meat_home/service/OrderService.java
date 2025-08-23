package com.backend.meat_home.service;

import com.backend.meat_home.dto.OrderRequestDTO;
import com.backend.meat_home.dto.OrderResponseDTO;
import com.backend.meat_home.dto.OrderItemDTO;
import com.backend.meat_home.dto.RateRequestDTO;
import com.backend.meat_home.entity.*;
import com.backend.meat_home.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderRateRepository orderRateRepository;

    // Place Order(Customer)
    public Order placeOrder(Long customerId, OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setAddress(orderRequestDTO.getAddress());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("PENDING");

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemDTO itemDTO : orderRequestDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(product.getId());
            item.setPrice(product.getPrice());
            item.setQuantity(itemDTO.getQuantity());
            item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

            totalPrice.add(item.getTotalPrice());
            orderItems.add(item);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        // Add status: PLACED
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrder(savedOrder);
        orderStatus.setStatus("PLACED");
        orderStatus.setTime(LocalDateTime.now());
        orderStatusRepository.save(orderStatus);

        return savedOrder;
    }

    // View pending Orders(Call Center)
    public List<Order> getPendingOrders() {
        return orderRepository.findByStatus("PENDING");
    }

    // Confirm Orders(Call Center)
    public Order confirmOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    order.setStatus("CONFIRMED");

    OrderStatus statusRecord = new OrderStatus();
    statusRecord.setOrder(order);
    statusRecord.setStatus("CONFIRMED");
    statusRecord.setTime(LocalDateTime.now());

    orderStatusRepository.save(statusRecord);

    return orderRepository.save(order);
    }

    // Track Order(Customer)
    public String trackOrder(Long orderId, Long customerId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getCustomerId().equals(customerId)) {
            throw new RuntimeException("You are not allowed to view this order");
        }

        return order.getStatus();
    }

    // Cancel Order(Admin)
    public Order cancelOrder(Long orderId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    order.setStatus("CANCELLED");
    orderRepository.save(order);

    OrderStatus history = new OrderStatus();
    history.setOrder(order);
    history.setStatus("CANCELLED");
    history.setTime(LocalDateTime.now());
    orderStatusRepository.save(history);

    return order;
    }

    // Rate Order(Customer)
    public OrderRate rateOrder(Long orderId, Long customerId, RateRequestDTO request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getCustomerId() == null || !order.getCustomerId().equals(customerId)) {
            throw new RuntimeException("You are not allowed to rate this order");
        }

        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException("Cannot rate a cancelled order");
        }

        if (orderRateRepository.findByOrderOrderId(orderId).isPresent()) {
            throw new RuntimeException("This order has already been rated");
        }

        int rate = request.getRate();
        if (rate < 1 || rate > 5) {
            throw new RuntimeException("Rate must be between 1 and 5");
        }

        OrderRate orderRate = new OrderRate();
        orderRate.setOrder(order);
        orderRate.setRate(rate);
        orderRate.setReview(request.getReview());
        orderRate.setVisible(true);
        orderRate.setCreatedAt(LocalDateTime.now());

        return orderRateRepository.save(orderRate);
    }

    // View & Hide Review(Admin)
    public OrderRate toggleVisibility(Long id, boolean visible) {
        OrderRate orderRate = orderRateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        orderRate.setVisible(visible);
        return orderRateRepository.save(orderRate);
    }

    // Get all Orders of CustomerId(Customer)
    public List<OrderResponseDTO> getOrdersByCustomer(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);

        return orders.stream().map(order -> new OrderResponseDTO(
                order.getOrderId(),
                order.getCreatedAt(),
                order.getTotalPrice(),
                order.getOrderItems()   
        )).collect(Collectors.toList());
    }

    // View Ready Orders(Drivers)
    public List<Order> getReadyOrders() {
        return orderRepository.findByStatus("READY");
    }

    // Accept Order(Driver)
    public Order acceptOrder(Long orderId, Long driverId) {
    Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException("Order not found"));

    order.setStatus("ON WAY");   
    order.setDriverId(driverId);

    OrderStatus statusRecord = new OrderStatus();
    statusRecord.setOrder(order);
    statusRecord.setStatus("ON WAY");
    statusRecord.setTime(LocalDateTime.now());

    orderStatusRepository.save(statusRecord);

    return orderRepository.save(order);
}


}


