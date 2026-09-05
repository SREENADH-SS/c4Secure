package com.backend.c4s.Repository;

import com.backend.c4s.Entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand , Long> {

    Optional<Brand>findByName(String name);
    boolean existByName(String name);
}
