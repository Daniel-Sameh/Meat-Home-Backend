package com.backend.meat_home.service;

import com.backend.meat_home.dto.*;
import com.backend.meat_home.entity.*;
import com.backend.meat_home.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final OrderRateRepository orderRateRepository;
    private final UserRepository userRepository;
    // private final ProductStock productStock;
    // private final ProductStockRepository productStockRepository;

    // Place Order(Customer)
    public Order placeOrder(OrderRequestDTO orderRequestDTO) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        if (User.Status.INACTIVE == user.getStatus()) {
            throw new IllegalStateException("Inactive users cannot place orders");
        }

        Order order = new Order();
        order.setCustomer(user);
        order.setAddress(orderRequestDTO.getAddress());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemDTO itemDTO : orderRequestDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new NoSuchElementException("Product with ID " + itemDTO.getProductId() + " not found"));

            //Uncomment when Create ProductStock !!
//            productStock.setStock(product.getStock() - itemDTO.getQuantity());
//            productStockRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(product.getId());
            item.setPrice(product.getPrice());
            item.setQuantity(itemDTO.getQuantity());
            item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));

            totalPrice = totalPrice.add(item.getTotalPrice());
            orderItems.add(item);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice.doubleValue());

        Order savedOrder = orderRepository.save(order);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrder(savedOrder);
        orderStatus.setStatus(Order.Status.PENDING);
        orderStatus.setTime(LocalDateTime.now());
        orderStatusRepository.save(orderStatus);

        return savedOrder;
    }

    // View pending Orders(Call Center)
    public List<Order> getPendingOrders() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        if (User.Status.INACTIVE == user.getStatus()) {
            throw new IllegalStateException("Inactive users cannot view pending orders");
        }
        return orderRepository.findByStatus(Order.Status.PENDING);
    }

    // Confirm Orders(Call Center)
    public Order confirmOrder(Long orderId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        if (User.Status.INACTIVE == user.getStatus()) {
            throw new IllegalStateException("Inactive users cannot confirm orders");
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));

        order.setStatus(Order.Status.CONFIRMED);

        OrderStatus statusRecord = new OrderStatus();
        statusRecord.setOrder(order);
        statusRecord.setStatus(Order.Status.CONFIRMED);
        statusRecord.setTime(LocalDateTime.now());

        orderStatusRepository.save(statusRecord);

        return orderRepository.save(order);
    }

    // Track Order(Customer)
    public Order.Status trackOrder(Long orderId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        if (User.Status.INACTIVE == user.getStatus()) {
            throw new IllegalStateException("Inactive users cannot track orders");
        }

        Long customerId = user.getId();

        Order order = orderRepository.findByOrderIdAndCustomerId(orderId, customerId)
            .orElseThrow(() -> new NoSuchElementException("Order not found"));

        return order.getStatus();
    }

    // Cancel Order(Admin)
    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));

        order.setStatus(Order.Status.CANCELLED);
        orderRepository.save(order);

        OrderStatus history = new OrderStatus();
        history.setOrder(order);
        history.setStatus(Order.Status.CANCELLED);
        history.setTime(LocalDateTime.now());
        orderStatusRepository.save(history);

        return order;
    }

    // Rate Order(Customer)
    public OrderRate rateOrder(Long orderId, RateRequestDTO request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Long customerId = user.getId();

        Order order = orderRepository.findByOrderIdAndCustomerId(orderId, customerId)
            .orElseThrow(() -> new NoSuchElementException("Order not found"));
        if (User.Status.INACTIVE == user.getStatus()) {
            throw new IllegalStateException("Inactive users cannot rate orders");
        }

        if ((Order.Status.DELIVERED) != (order.getStatus())) {
        throw new IllegalStateException("You can only rate an order after it is delivered");
        }

        if (orderRateRepository.findByOrderOrderId(orderId).isPresent()) {
            throw new IllegalStateException("This order has already been rated");
        }

        int rate = request.getRate();
        if (rate < 1 || rate > 5) {
            throw new IllegalArgumentException("Rate must be between 1 and 5");
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
    public void toggleVisibility(Long id, boolean visible) {
        OrderRate orderRate = orderRateRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Review not found"));

        orderRate.setVisible(visible);
        orderRateRepository.save(orderRate);
    }

    // Get all Orders of CustomerId(Customer)
    public List<OrderResponseDTO> getOrdersByCustomer() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        Long customerId = user.getId();

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
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        if (User.Status.INACTIVE == user.getStatus()) {
            throw new IllegalStateException("Inactive users cannot view ready orders");
        }
        return orderRepository.findByStatus(Order.Status.READY);
    }

    // Accept Order(Driver)
    public void acceptOrder(Long orderId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User driver = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Driver not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        if (User.Status.INACTIVE == driver.getStatus()) {
            throw new IllegalStateException("Inactive users cannot accept orders");
        }

        order.setStatus(Order.Status.ASSIGNED);
        order.setDriver(driver);

        OrderStatus statusRecord = new OrderStatus();
        statusRecord.setOrder(order);
        statusRecord.setStatus(Order.Status.ASSIGNED);
        statusRecord.setTime(LocalDateTime.now());
        orderStatusRepository.save(statusRecord);

        orderRepository.save(order);
    }

    // Change Order Status(Admin & Call Center & Driver)
    public Order changeOrderStatus(Long orderId, Order.Status newStatus, String role) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));

        Order.Status currentStatus = order.getStatus();

        switch (role) {
            case "CALL_CENTER_AGENT" -> {
                if (currentStatus.equals(Order.Status.CONFIRMED) && newStatus.equals(Order.Status.IN_PREPARATION)) {
                    order.setStatus(Order.Status.IN_PREPARATION);
                } else if (currentStatus.equals(Order.Status.IN_PREPARATION) && newStatus.equals(Order.Status.READY)) {
                    order.setStatus(Order.Status.READY);
                } else {
                    throw new IllegalStateException("Call Center cannot change status from " + currentStatus + " to " + newStatus);
                }
            }
            case "DRIVER" -> {
                String email = SecurityContextHolder.getContext().getAuthentication().getName();
                User driver = userRepository.findByEmail(email)
                        .orElseThrow(() -> new NoSuchElementException("Driver not found"));

                if (order.getDriver() == null || !order.getDriver().getId().equals(driver.getId())) {
                    throw new SecurityException("This driver is not assigned to this order");
                }
                if (currentStatus.equals(Order.Status.ASSIGNED) && newStatus.equals(Order.Status.ON_WAY)) {
                    order.setStatus(Order.Status.ON_WAY);
                }
                else if (currentStatus.equals(Order.Status.ON_WAY) && newStatus.equals(Order.Status.DELIVERED)) {
                    order.setStatus(Order.Status.DELIVERED);
                } else {
                    throw new IllegalStateException("Driver cannot change status from " + currentStatus + " to " + newStatus);
                }
            }
            case "ADMIN" -> order.setStatus(newStatus);
            default -> throw new SecurityException("Role " + role + " is not allowed to change order status");
        }

        Order savedOrder = orderRepository.save(order);

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrder(savedOrder);
        orderStatus.setStatus(newStatus);
        orderStatus.setTime(LocalDateTime.now());
        orderStatusRepository.save(orderStatus);

        return savedOrder;
    }

    // Reassign Driver
    public Order reassignDriver(Long orderId, Long newDriverId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));

        if ((Order.Status.ASSIGNED) != (order.getStatus())) {
            throw new IllegalStateException("Order is not in ASSIGNED status");
        }

        if (order.getDriver() == null) {
            throw new IllegalStateException("Order does not have a driver assigned yet");
        }

        User driver = userRepository.findById(newDriverId)
                .orElseThrow(() -> new NoSuchElementException("Driver not found"));

        order.setDriver(driver);

        return orderRepository.save(order);
    }
}


