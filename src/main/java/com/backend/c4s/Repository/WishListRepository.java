package com.backend.c4s.Repository;

import com.backend.c4s.Entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    Optional<WishList>findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
