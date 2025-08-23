package com.backend.meat_home.controller;

import com.backend.meat_home.dto.OrderRequestDTO;
import com.backend.meat_home.dto.OrderResponseDTO;
import com.backend.meat_home.dto.RateRequestDTO;
import com.backend.meat_home.entity.Order;
import com.backend.meat_home.entity.OrderRate;
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
        Long customerId = 1L;
        return orderService.placeOrder(customerId, orderRequestDTO);
    }
    // View Orders
    @GetMapping("/pending")
    public List<Order> getUpcomingOrders() {
        return orderService.getUpcomingOrders();
    }

    // Confirm Orders
    @PutMapping("/confirm/{orderId}")
    public Order confirmOrder(@PathVariable Long orderId) {
        return orderService.confirmOrder(orderId);
    }

    // Track Order
    @GetMapping("/track/{orderId}")
    public String trackOrder(@PathVariable Long orderId,
                            @RequestParam Long customerId) {
        return orderService.trackOrder(orderId, customerId);
    }

    //Cancel Order
    @PutMapping("/cancel/{orderId}")
    public Order cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    // Rate Order
    @PostMapping("/rate/{orderId}")
    public ResponseEntity<?> rateOrder(@PathVariable Long orderId,
                                       @RequestParam Long customerId,
                                       @RequestBody RateRequestDTO request) {

        OrderRate saved = orderService.rateOrder(orderId, customerId, request);
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
    public ResponseEntity<List<OrderResponseDTO>> getCustomerOrders(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }
    
}
