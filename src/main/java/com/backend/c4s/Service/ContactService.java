package com.backend.c4s.Service;

import com.backend.c4s.Dto.Contact.ContactRequest;
import com.backend.c4s.Dto.Contact.ContactResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactService {

    ContactResponse createContact(ContactRequest request);

    ContactResponse getContactById(Long id);

    Page<ContactResponse> getAlContact(Pageable pageable);

    Page<ContactResponse> getContactByStatus(Boolean  isResolved, Pageable pageable);

    ContactResponse toggleResolveStatus(Long id);

    void delectContact(Long id);
}
