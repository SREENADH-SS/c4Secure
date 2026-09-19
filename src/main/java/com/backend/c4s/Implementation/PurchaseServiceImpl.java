package com.backend.c4s.Implementation;

import com.backend.c4s.Dto.Purchase.PurchaseAdminResponse;
import com.backend.c4s.Dto.Purchase.PurchaseUserResponse;
import com.backend.c4s.Dto.PurchaseItem.PurchaseItemRequest;
import com.backend.c4s.Entity.Products;
import com.backend.c4s.Entity.Purchase;
import com.backend.c4s.Entity.PurchaseItem;
import com.backend.c4s.Entity.Users;
import com.backend.c4s.Entity.common.PurchaseStatus;
import com.backend.c4s.Exception.ResourceNotFoundException;
import com.backend.c4s.Mapper.PurchaseMapper;
import com.backend.c4s.Repository.ProductRepository;
import com.backend.c4s.Repository.PurchaseRepository;
import com.backend.c4s.Repository.UserRepository;
import com.backend.c4s.Service.PurchaseService;
import com.backend.c4s.Utility.PurchaseIdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional

public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PurchaseMapper purchaseMapper;
    private final PurchaseIdGenerator purchaseIdGenerator;

    @Override
    public PurchaseUserResponse createPurchase(Long userId, String shippingAddress, List<PurchaseItemRequest> items,boolean requiresInstallation) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Purchase must contain at least one item");
        }
        Users user= userRepository.findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("user", "id", userId));


        PurchaseStatus initialStatus= requiresInstallation ? PurchaseStatus.PENDING_INSTALLATION :PurchaseStatus.PLACED;

        Purchase purchase= Purchase.builder()
                .purchaseId(purchaseIdGenerator.generatePurchaseId())
                .user(user)
                .shippingAddress(shippingAddress)
                .status(initialStatus)
                .item(new ArrayList<>())
                .build();

        BigDecimal totalAmount= BigDecimal.ZERO;

        for (PurchaseItemRequest itemRequest: items){
            Products product= productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(()->new ResourceNotFoundException("product", "id", itemRequest.getProductId()));

            if (product.getStockQuantity()< itemRequest.getQuantity()){
                throw new IllegalStateException("Insufficient stock for product: " + product.getName()
                +". Available: " + product.getStockQuantity()
                +", Requested: " + itemRequest.getQuantity());
            }

            product.setStockQuantity(product.getStockQuantity()-itemRequest.getQuantity());
            productRepository.save(product);

            BigDecimal itemTotal= product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount= totalAmount.add(itemTotal);

            PurchaseItem purchaseItem = PurchaseItem.builder()
                    .purchase(purchase)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .build();
            purchase.getItem().add(purchaseItem);
        }

        purchase.setTotalAmount(totalAmount);

        Purchase savedPurchase= purchaseRepository.save(purchase);

        return purchaseMapper.toPurchaseUserResponse(savedPurchase);
    }

    @Override
    @Transactional
    public PurchaseUserResponse getPurchaseByIdForUUser(String purchaseId, Long userId) {
        Purchase purchase= purchaseRepository.findById(purchaseId)
                .orElseThrow(()->new ResourceNotFoundException("purchase", "id", purchaseId));
        if (!purchase.getUser().getId().equals(userId)){
            throw new SecurityException("Access denied: Order does not belong to user");
        }
        return purchaseMapper.toPurchaseUserResponse(purchase);
    }

    @Override
    @Transactional
    public List<PurchaseUserResponse> getUserPurchases(Long userId) {
        return purchaseRepository.findByUserId(userId).stream()
                .map(purchaseMapper::toPurchaseUserResponse)
                .toList();
    }

    @Override
    @Transactional
    public PurchaseUserResponse cancelOrder(String purchaseId, Long userId) {
        Purchase purchase= purchaseRepository.findById(purchaseId)
                .orElseThrow(()->new ResourceNotFoundException("purchase", "id", purchaseId));

        if (!purchase.getUser().getId().equals(userId)){
            throw new SecurityException("You are not allowed to cancel this Order");
        }

        if (purchase.getStatus()!=PurchaseStatus.PLACED &&
               purchase.getStatus()!= PurchaseStatus.PENDING_INSTALLATION
        ){
            throw new IllegalStateException("Order cannot be cancelled at this stage");
        }

        restoreProductStock(purchase);

        purchase.setStatus(PurchaseStatus.CANCELLED);

        Purchase cancelledPurchase= purchaseRepository.save(purchase);

        return purchaseMapper.toPurchaseUserResponse(cancelledPurchase);
    }


    @Override
    @Transactional(readOnly = true)
    public List<PurchaseAdminResponse> getAllPurChasesList() {
        return purchaseRepository.findAll().stream()
                .map(purchaseMapper::toPurchaseAdminResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PurchaseAdminResponse getPurchaseByIdForAdmin(String purchaseId) {
        Purchase purchase= purchaseRepository.findById(purchaseId)
                .orElseThrow(()->new ResourceNotFoundException("purchase","id", purchaseId));

        return purchaseMapper.toPurchaseAdminResponse(purchase);

    }

    @Override
    @Transactional
    public PurchaseAdminResponse updatePurchaseStatus(String purchaseId, PurchaseStatus status) {

        Purchase purchase= purchaseRepository.findById(purchaseId)
                .orElseThrow(()->new ResourceNotFoundException("purchase", "id", purchaseId));

        if (status == PurchaseStatus.CANCELLED && purchase.getStatus()!= PurchaseStatus.CANCELLED){
            restoreProductStock(purchase);
        }

        purchase.setStatus(status);

        Purchase updatedPurchase= purchaseRepository.save(purchase);

        return purchaseMapper.toPurchaseAdminResponse(updatedPurchase);
    }


    private void restoreProductStock(Purchase purchase) {

        for (PurchaseItem item: purchase.getItem()){
            Products product= item.getProduct();
            product.setStockQuantity(product.getStockQuantity()+item.getQuantity());
            productRepository.save(product);
        }
    }


}
