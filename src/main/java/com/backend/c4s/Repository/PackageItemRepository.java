package com.backend.c4s.Repository;

import com.backend.c4s.Entity.PackageItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageItemRepository extends JpaRepository<PackageItem, Long> {
}
