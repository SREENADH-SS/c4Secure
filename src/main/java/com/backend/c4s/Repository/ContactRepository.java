package com.backend.c4s.Repository;

import com.backend.c4s.Entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    Page<Contact> findByIsResolved(Boolean isResolved, Pageable pageable);
}
