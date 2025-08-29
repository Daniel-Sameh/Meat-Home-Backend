package com.backend.meat_home.service;

import com.backend.meat_home.dto.CartItemRequest;
import com.backend.meat_home.entity.Cart;
import com.backend.meat_home.entity.CartItem;
import com.backend.meat_home.entity.Product;
import com.backend.meat_home.entity.User;
import com.backend.meat_home.exception.ResourceNotFoundException;
import com.backend.meat_home.repository.CartItemRepository;
import com.backend.meat_home.repository.CartRepository;
import com.backend.meat_home.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    // Add Item to Cart
    public Cart addItem(CartItemRequest cartItemRequest) {

        //Getting the user cart or making or not if not exists.
        Cart cart = cartRepository.findByCustomerId_Id(cartItemRequest.getCustomerId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    User user = new User();
                    user.setId(cartItemRequest.getCustomerId());
                    newCart.setCustomerId(user);
                    return cartRepository.save(newCart);
                });

        //Getting product by productId
        Product product = productService.getProductById(cartItemRequest.getProductId());


        //Finding the cart item by cartId and productId, or creating a new one if it doesn't exist
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElseGet(() -> {
                    CartItem newCartItem = new CartItem();
                    newCartItem.setCart(cart);
                    newCartItem.setProduct(product);
                    return newCartItem;
                });

        //Setting the product item quantity (in kilos)
        cartItem.setQuantity(cartItemRequest.getQuantity());
        cartItemRepository.save(cartItem);
        return cart;
    }

    // Update Quantity
    public CartItem updateCartItemQuantity(Long itemId, float quantity){
        //Getting the cart item by the item id
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + itemId));

        if (quantity<=0){
            //If the quantity is less than or equal to zero, delete the cart item
            cartItemRepository.deleteById(itemId);
            return null;
        }

        //Updating the quantity of the cart item
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    // View Cart
    public List<CartItem> viewCart(Long customerId) {
        //Finding the cart by customerId
        Cart cart = cartRepository.findByCustomerId_Id(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user with id: " + customerId));

        //Returning the list of cart items
        return cartItemRepository.findAllByCartId(cart.getId());
    }

    //Reset Cart
    public Boolean resetCart(Long customerId){
        //Finding the cart by customerId
        Cart cart = cartRepository.findByCustomerId_Id(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user with id: " + customerId));

        //Deleting all items in the cart
        cartItemRepository.deleteAllByCartId(cart.getId());

        //Deleting the cart itself
        cartRepository.deleteById(cart.getId());

        return true;
    }
}
