package com.backend.c4s.Repository;

import com.backend.c4s.Entity.Products;
import com.backend.c4s.Entity.common.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Products, Long>, JpaSpecificationExecutor<Products> {

    @Query("SELECT DISTINCT p FROM Products p " +
            "LEFT JOIN p.categories c " +
            "LEFT JOIN p.brand b " +
            "WHERE p.productStatus = :status " +
            "AND (:brandIds IS NULL OR b.id IN :brandIds) " +
            "AND (:categoryIds IS NULL OR c.id IN :categoryIds) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<Products>filterProducts(
            @Param("status") ProductStatus status,
            @Param("brandIds") List<Long>brandIds,
            @Param("categoryIds") List<Long>categoryIds,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable
    );
}
