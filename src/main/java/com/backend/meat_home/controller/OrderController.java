package com.backend.meat_home.controller;

import com.backend.meat_home.dto.OrderRequestDTO;
import com.backend.meat_home.entity.Order;
import com.backend.meat_home.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // Places order
    @PostMapping("/place")
    public Order placeOrder(@RequestBody OrderRequestDTO orderRequestDTO,
                            @RequestParam Long customerId) {
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

    
}
