package com.backend.c4s.Implementation;

import com.backend.c4s.Dto.Cart.AddToCartRequest;
import com.backend.c4s.Dto.Cart.CartResponse;
import com.backend.c4s.Dto.Cart.UpdateCartItemRequest;
import com.backend.c4s.Entity.Cart;
import com.backend.c4s.Entity.CartItem;
import com.backend.c4s.Entity.Products;
import com.backend.c4s.Entity.Users;
import com.backend.c4s.Mapper.CartMapper;
import com.backend.c4s.Repository.CartItemRepository;
import com.backend.c4s.Repository.CartRepository;
import com.backend.c4s.Repository.ProductRepository;
import com.backend.c4s.Repository.UserRepository;
import com.backend.c4s.Service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCartByUserId(Long userId) {
        Cart cart= getOrCreateCartEntity(userId);
        return cartMapper.toCartResponse(cart);
    }


    @Override
    public CartResponse addToCart(Long userId, AddToCartRequest request) {

        Cart cart= getOrCreateCartEntity(userId);

        Products product = productRepository.findById(request.getProductId())
                .orElseThrow(()-> new RuntimeException("Product not found with id: " + request.getProductId()));

        Optional<CartItem> existingItemOpt= cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId());

        if (existingItemOpt.isPresent()){
            CartItem existingItem= existingItemOpt.get();
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
        }
        else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            cart.getItems().add(newItem);
            cartItemRepository.save(newItem);
        }
        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse updateCartItemQuantity(Long userId, Long cartItemId, UpdateCartItemRequest request) {
        Cart cart= getOrCreateCartEntity(userId);

        CartItem cartItem= cartItemRepository.findById(cartItemId)
                .orElseThrow(()-> new RuntimeException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Unauthorized action on this cart item");
        }
            cartItem.setQuantity(request.getQuantity());
            cartItemRepository.save(cartItem);

            return cartMapper.toCartResponse(cartRepository.save(cart));

    }

    @Override
    public CartResponse removeCartItem(Long userId, Long cartItemId) {
        Cart cart= getOrCreateCartEntity(userId);

        CartItem cartItem= cartItemRepository.findById(cartItemId)
                .orElseThrow(()->new RuntimeException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getCart().getId().equals(cart.getId())){
            throw new RuntimeException("Unauthorized action on this cart item");
        }
        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Override
    public void clearCart(Long userId) {
        Cart cart= getOrCreateCartEntity(userId);
        cart.getItems().clear();
        cartRepository.save(cart);

    }

    private Cart getOrCreateCartEntity(Long userId) {

        return cartRepository.findByUserIdWithItems(userId)
                .orElseGet(()->{
                    Users user= userRepository.findById(userId)
                            .orElseThrow(()-> new RuntimeException("User not found with id: " + userId));
                    Cart newCart= Cart.builder()
                            .user(user)
                            .build();
                    return cartRepository.save(newCart);
                });
    }
}
