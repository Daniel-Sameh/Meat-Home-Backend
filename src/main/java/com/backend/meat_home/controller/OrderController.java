package com.backend.meat_home.controller;

import com.backend.meat_home.dto.*;
import com.backend.meat_home.entity.*;
import com.backend.meat_home.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Places order
    @PostMapping("/place")
    public Order placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        return orderService.placeOrder(orderRequestDTO);
    }

    // View Orders
    @GetMapping("/pending")
    public List<Order> getPendingOrders() {
        return orderService.getPendingOrders();
    }

    // Confirm Orders
    @PutMapping("/confirm/{orderId}")
    public Order confirmOrder(@PathVariable Long orderId) {
        return orderService.confirmOrder(orderId);
    }

    // Track Order
    @GetMapping("/track/{orderId}")
    public String trackOrder(@PathVariable Long orderId) {
        return orderService.trackOrder(orderId);
    }

    //Cancel Order
    @PutMapping("/cancel/{orderId}")
    public Order cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    // Rate Order
    @PostMapping("/rate/{orderId}")
    public ResponseEntity<?> rateOrder(@PathVariable Long orderId,
                                       @RequestBody RateRequestDTO request) {
        OrderRate saved = orderService.rateOrder(orderId, request);
        return ResponseEntity.status(201).body(saved);
    }

    // View & Hide Reviews
    @PatchMapping("/visibility/{rateId}")
    public ResponseEntity<String> toggleVisibility(@PathVariable Long rateId,
                                                   @RequestParam boolean visible) {
        orderService.toggleVisibility(rateId, visible);
        return ResponseEntity.ok("Review visibility updated successfully");
    }

    // Get Orders
    @GetMapping("/all-orders/{customerId}")
    public ResponseEntity<List<OrderResponseDTO>> getCustomerOrders() {
        return ResponseEntity.ok(orderService.getOrdersByCustomer());
    }

    // View Orders
    @GetMapping("/ready")
    public ResponseEntity<List<Order>> getReadyOrders() {
        return ResponseEntity.ok(orderService.getReadyOrders());
    }

    // Accept Orders
    @PutMapping("/accept/{orderId}")
    public ResponseEntity<String> acceptOrder(@PathVariable Long orderId) {
        orderService.acceptOrder(orderId);
        return ResponseEntity.ok("Order accepted successfully");
    }

}
