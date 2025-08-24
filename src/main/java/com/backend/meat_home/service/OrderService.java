package com.backend.meat_home.service;

import com.backend.meat_home.dto.OrderRequestDTO;
import com.backend.meat_home.dto.OrderItemDTO;
import com.backend.meat_home.entity.*;
import com.backend.meat_home.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderStatusRepository orderStatusRepository;

    // Place Order(Customer)
    public Order placeOrder(Long customerId, OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setAddress(orderRequestDTO.getAddress());
        order.setCreatedAt(LocalDateTime.now());

        order.setStatus("PENDING");

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (OrderItemDTO itemDTO : orderRequestDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new NoSuchElementException("Product with ID " + itemDTO.getProductId() + " not found"));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(product.getId());
            item.setPrice(product.getPrice());
            item.setQuantity(itemDTO.getQuantity());
            item.setTotalPrice(product.getPrice() * itemDTO.getQuantity());

            totalPrice += item.getTotalPrice();
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
    public List<Order> getUpcomingOrders() {
        return orderRepository.findByStatus("PENDING");
    }


    // Confirm Orders(Call Center)
    public Order confirmOrder(Long orderId) {
      Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new NoSuchElementException("Order with ID " + orderId + " not found"));

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
                .orElseThrow(() -> new NoSuchElementException("Order with ID " + orderId + " not found"));

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
}
