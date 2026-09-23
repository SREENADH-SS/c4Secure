package com.backend.c4s.Implementation;

import com.backend.c4s.Dto.Purchase.PackagePurchaseRequest;
import com.backend.c4s.Dto.Purchase.PurchaseAdminResponse;
import com.backend.c4s.Dto.Purchase.PurchaseUserResponse;
import com.backend.c4s.Dto.PurchaseItem.PurchaseItemRequest;
import com.backend.c4s.Entity.*;
import com.backend.c4s.Entity.Package;
import com.backend.c4s.Entity.common.PurchaseStatus;
import com.backend.c4s.Exception.ResourceNotFoundException;
import com.backend.c4s.Mapper.PurchaseMapper;
import com.backend.c4s.Repository.*;
import com.backend.c4s.Service.CartService;
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
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final PackageRepository packageRepository;

    @Override
    public PurchaseUserResponse createPurchase(Long userId, String shippingAddress, List<PurchaseItemRequest> items, boolean requiresInstallation) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Purchase must contain at least one item");
        }
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));


        PurchaseStatus initialStatus = requiresInstallation ? PurchaseStatus.PENDING_INSTALLATION : PurchaseStatus.PLACED;

        Purchase purchase = Purchase.builder()
                .purchaseId(purchaseIdGenerator.generatePurchaseId())
                .user(user)
                .shippingAddress(shippingAddress)
                .status(initialStatus)
                .item(new ArrayList<>())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (PurchaseItemRequest itemRequest : items) {
            Products product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("product", "id", itemRequest.getProductId()));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName()
                        + ". Available: " + product.getStockQuantity()
                        + ", Requested: " + itemRequest.getQuantity());
            }

            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            PurchaseItem purchaseItem = PurchaseItem.builder()
                    .purchase(purchase)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .priceAtPurchase(product.getPrice())
                    .build();
            purchase.getItem().add(purchaseItem);
        }

        purchase.setTotalAmount(totalAmount);

        Purchase savedPurchase = purchaseRepository.save(purchase);

        return purchaseMapper.toPurchaseUserResponse(savedPurchase);
    }

    @Override
    public PurchaseUserResponse createPurchaseFromCart(Long userId, String shippingAddress, boolean requiresInstallation) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("cart", "userId", userId));

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot process purchase: Cart is empty");
        }

        PurchaseStatus initialStatus = requiresInstallation ? PurchaseStatus.PENDING_INSTALLATION : PurchaseStatus.PLACED;

        Purchase purchase = Purchase.builder()
                .purchaseId(purchaseIdGenerator.generatePurchaseId())
                .user(user)
                .shippingAddress(shippingAddress)
                .status(initialStatus)
                .item(new ArrayList<>())
                .build();

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Products product = cartItem.getProduct();
            int requestedQuantity = cartItem.getQuantity();

            if (product.getStockQuantity() < requestedQuantity) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName()
                        + ". Available: " + product.getStockQuantity()
                        + ", Requested: " + requestedQuantity);
            }

            // Deduct stock
            product.setStockQuantity(product.getStockQuantity() - requestedQuantity);
            productRepository.save(product);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(requestedQuantity));
            totalAmount = totalAmount.add(itemTotal);

            PurchaseItem purchaseItem = PurchaseItem.builder()
                    .purchase(purchase)
                    .product(product)
                    .quantity(requestedQuantity)
                    .priceAtPurchase(product.getPrice())
                    .build();

            purchase.getItem().add(purchaseItem);
        }

        purchase.setTotalAmount(totalAmount);
        Purchase savedPurchase = purchaseRepository.save(purchase);

        // Empty the cart after successful order creation
        cartService.clearCart(userId);

        return purchaseMapper.toPurchaseUserResponse(savedPurchase);
    }

    @Override
    public PurchaseUserResponse createPackagePurchase(Long userId, PackagePurchaseRequest request) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));

        Package offerPackage = packageRepository.findById(request.getPackageId())
                .orElseThrow(() -> new ResourceNotFoundException("package", "id", request.getPackageId()));

        if (!offerPackage.isActive()) {
            throw new IllegalStateException("This package is currently inactive and cannot be purchased.");
        }

        if (offerPackage.getItems() == null || offerPackage.getItems().isEmpty()) {
            throw new IllegalStateException("Package does not contain any products.");
        }

        boolean requiresInstallation = Boolean.TRUE.equals(request.getRequiresInstallation());
        PurchaseStatus initialStatus = requiresInstallation ? PurchaseStatus.PENDING_INSTALLATION : PurchaseStatus.PLACED;

        Purchase purchase = Purchase.builder()
                .purchaseId(purchaseIdGenerator.generatePurchaseId())
                .user(user)
                .shippingAddress(request.getShippingAddress())
                .status(initialStatus)
                .item(new ArrayList<>())
                .build();

        // Check stock availability & deduct for each item in the package
        for (PackageItem pkgItem : offerPackage.getItems()) {
            Products product = pkgItem.getProduct();
            int requiredQty = pkgItem.getQuantity();

            if (product.getStockQuantity() < requiredQty) {
                throw new IllegalStateException("Insufficient stock for packaged product: " + product.getName()
                        + ". Available: " + product.getStockQuantity()
                        + ", Requested: " + requiredQty);
            }

            // Deduct product stock
            product.setStockQuantity(product.getStockQuantity() - requiredQty);
            productRepository.save(product);

            // Record item against the purchase order
            PurchaseItem purchaseItem = PurchaseItem.builder()
                    .purchase(purchase)
                    .product(product)
                    .quantity(requiredQty)
                    .priceAtPurchase(product.getPrice())
                    .build();

            purchase.getItem().add(purchaseItem);
        }

        // Total amount = Package Price + Installation Charge
        BigDecimal packagePrice = offerPackage.getPackagePrice() != null ? offerPackage.getPackagePrice() : BigDecimal.ZERO;
        BigDecimal installationCharge = (requiresInstallation && offerPackage.getInstallationCharge() != null)
                ? offerPackage.getInstallationCharge()
                : BigDecimal.ZERO;

        purchase.setTotalAmount(packagePrice.add(installationCharge));

        Purchase savedPurchase = purchaseRepository.save(purchase);

        return purchaseMapper.toPurchaseUserResponse(savedPurchase);
    }


    @Override
    @Transactional
    public PurchaseUserResponse getPurchaseByIdForUser(String purchaseId, Long userId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("purchase", "id", purchaseId));
        if (!purchase.getUser().getId().equals(userId)) {
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
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("purchase", "id", purchaseId));

        if (!purchase.getUser().getId().equals(userId)) {
            throw new SecurityException("You are not allowed to cancel this Order");
        }

        if (purchase.getStatus() != PurchaseStatus.PLACED &&
                purchase.getStatus() != PurchaseStatus.PENDING_INSTALLATION
        ) {
            throw new IllegalStateException("Order cannot be cancelled at this stage");
        }

        restoreProductStock(purchase);

        purchase.setStatus(PurchaseStatus.CANCELLED);

        Purchase cancelledPurchase = purchaseRepository.save(purchase);

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
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("purchase", "id", purchaseId));

        return purchaseMapper.toPurchaseAdminResponse(purchase);

    }

    @Override
    @Transactional
    public PurchaseAdminResponse updatePurchaseStatus(String purchaseId, PurchaseStatus status) {

        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new ResourceNotFoundException("purchase", "id", purchaseId));

        if (status == PurchaseStatus.CANCELLED && purchase.getStatus() != PurchaseStatus.CANCELLED) {
            restoreProductStock(purchase);
        }

        purchase.setStatus(status);

        Purchase updatedPurchase = purchaseRepository.save(purchase);

        return purchaseMapper.toPurchaseAdminResponse(updatedPurchase);
    }


    private void restoreProductStock(Purchase purchase) {

        for (PurchaseItem item : purchase.getItem()) {
            Products product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }
    }


}
