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

    // Place Order
    public Order placeOrder(Long customerId, OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setAddress(orderRequestDTO.getAddress());
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        double totalPrice = 0.0;

        for (OrderItemDTO itemDTO : orderRequestDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

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

   
}
