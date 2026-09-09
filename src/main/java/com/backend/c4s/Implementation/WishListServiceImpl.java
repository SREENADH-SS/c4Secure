package com.backend.c4s.Implementation;

import com.backend.c4s.Dto.Cart.AddToCartRequest;
import com.backend.c4s.Dto.WishList.WishListRequest;
import com.backend.c4s.Dto.WishList.WishListResponse;
import com.backend.c4s.Entity.Products;
import com.backend.c4s.Entity.Users;
import com.backend.c4s.Entity.WishList;
import com.backend.c4s.Exception.ResourceNotFoundException;
import com.backend.c4s.Mapper.WishListMapper;
import com.backend.c4s.Repository.ProductRepository;
import com.backend.c4s.Repository.UserRepository;
import com.backend.c4s.Repository.WishListRepository;
import com.backend.c4s.Service.CartService;
import com.backend.c4s.Service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final UserRepository  userRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final WishListMapper wishListMapper;

    @Override
    @Transactional(readOnly = true)
    public WishListResponse getWishListByUserId(Long userId) {
        WishList wishList= getOrCreateWishListEntity(userId);
        return wishListMapper.toWishListResponse(wishList);
    }

    @Override
    public WishListResponse addProductToWishList(WishListRequest request) {
        WishList wishList= getOrCreateWishListEntity(request.getUserId());
        Products product= productRepository.findById(request.getProductId())
                .orElseThrow(()->new ResourceNotFoundException("product", "id", request.getProductId()));

        wishList.getProduct().add(product);
        WishList savedWishList= wishListRepository.save(wishList);
        return wishListMapper.toWishListResponse(savedWishList);
    }

    @Override
    public WishListResponse removeProductFromWishList(Long userId, Long productId) {
        WishList wishList= getWishListEntity(userId);
        Products product= productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("product", "id", productId));

        wishList.getProduct().remove(product);
        WishList updatedWishList= wishListRepository.save(wishList);
        return wishListMapper.toWishListResponse(updatedWishList);
    }

    @Override
    public WishListResponse clearWishList(Long userId) {
        WishList wishList= getWishListEntity(userId);
        wishList.getProduct().clear();
        WishList updatedWishList= wishListRepository.save(wishList);
        return wishListMapper.toWishListResponse(updatedWishList);
    }

    @Override
    public void addProductToCartFromWishList(Long userId, Long productId, Integer quantity) {

        WishList wishList= getWishListEntity(userId);

        Products product= wishList.getProduct().stream()
                .filter(p->p.getId().equals(productId))
                .findFirst()
                .orElseThrow(()->new ResourceNotFoundException("product","id", productId));

        int qtyToAdd= (quantity !=null && quantity>0)? quantity: 1;

        AddToCartRequest cartRequest= AddToCartRequest.builder()
                .productId(product.getId())
                .quantity(qtyToAdd)
                .build();

        cartService.addToCart(userId,cartRequest);

    }

    @Override
    public void moveAllWishListToCart(Long userId) {

        WishList wishList= getWishListEntity(userId);
        Set<Products> products = new HashSet<>(wishList.getProduct());

        if (products.isEmpty()){
            throw  new RuntimeException("Wishlist is empty");
        }

        for (Products product : products){

            AddToCartRequest cartRequest= AddToCartRequest.builder()
                    .productId(product.getId())
                    .quantity(1)
                    .build();
            cartService.addToCart(userId,cartRequest);
        }

        wishList.getProduct().clear();
        wishListRepository.save(wishList);

    }

    private WishList getWishListEntity(Long userId) {
        return wishListRepository.findByUserId(userId)
                .orElseThrow(()->new ResourceNotFoundException("user", "id", userId));
    }

    private WishList getOrCreateWishListEntity(Long userId) {
        return wishListRepository.findByUserId(userId).orElseGet(()->{
            Users user = userRepository.findById(userId)
                    .orElseThrow(()-> new ResourceNotFoundException("user", "id", userId));

            WishList newWishList =WishList.builder()
                    .user(user)
                    .product(new HashSet<>())
                    .build();
            return wishListRepository.save(newWishList);
        });
    }
}
