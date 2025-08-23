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

    @PostMapping("/add-item")
    public ResponseEntity<?> addItemToCart(@RequestBody CartItemRequest cartItemRequest) {
        logger.info("Adding item to cart: {}", cartItemRequest);
        try{
            Cart cart = cartService.addItem(cartItemRequest);
            return ResponseEntity.ok(cart);
        }catch (Exception e){
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(404).body(e.getMessage());
            } else {
                return ResponseEntity.status(500).body("An error occurred while adding item to cart: " + e.getMessage());
            }
        }
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long cartItemId, @RequestBody CartItemRequest cartItemRequest) {
        try {
            Optional<CartItem> cartItem = cartService.updateCartItemQuantity(cartItemId, cartItemRequest.getQuantity());
            return ResponseEntity.ok(cartItem);
        } catch (Exception e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(404).body(e.getMessage());
            } else {
                return ResponseEntity.status(500).body("An error occurred while updating the cart item: " + e.getMessage());
            }
        }
    }

    @GetMapping("/view/{customerId}")
    public ResponseEntity<?> viewCart(@PathVariable Long customerId) {
        try {
            List<CartItem> cart = cartService.viewCart(customerId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(404).body(e.getMessage());
            } else {
                return ResponseEntity.status(500).body("An error occurred while viewing the cart: " + e.getMessage());
            }
        }
    }

}
