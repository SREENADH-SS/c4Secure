package com.backend.c4s.Repository;

import com.backend.c4s.Entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage>findByProductId(Long productId);
    Optional<ProductImage>findByPublicId(String publicId);
}
