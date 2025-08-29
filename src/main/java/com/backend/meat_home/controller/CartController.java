package com.backend.meat_home.controller;

import com.backend.meat_home.dto.CartItemRequest;
import com.backend.meat_home.entity.Cart;
import com.backend.meat_home.entity.CartItem;
import com.backend.meat_home.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/cart")
@AllArgsConstructor
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    @Autowired
    private final CartService cartService;

    // Add Item
    @PostMapping("/add-item")
    public ResponseEntity<Cart> addItemToCart(@RequestBody CartItemRequest cartItemRequest) {
        logger.info("Adding item to cart: {}", cartItemRequest);
        return ResponseEntity.ok(cartService.addItem(cartItemRequest));
    }

    // Update Cart
    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@PathVariable Long cartItemId, @RequestBody CartItemRequest cartItemRequest) {
        logger.info("Updating cart item with ID {}: {}", cartItemId, cartItemRequest);
        return ResponseEntity.ok(cartService.updateCartItemQuantity(cartItemId, cartItemRequest.getQuantity()));
    }

    // Get Cart
    @GetMapping("/view/{customerId}")
    public ResponseEntity<List<CartItem>> viewCart(@PathVariable Long customerId) {
        logger.info("Viewing cart for customer ID: {}", customerId);
        return ResponseEntity.ok(cartService.viewCart(customerId));
    }
}
