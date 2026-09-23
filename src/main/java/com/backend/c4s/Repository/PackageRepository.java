package com.backend.c4s.Repository;

import com.backend.c4s.Entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {

    List<Package> findByActiveTrue();
}
