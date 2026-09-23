package com.backend.c4s.Implementation;

import com.backend.c4s.Dto.Contact.ContactRequest;
import com.backend.c4s.Dto.Contact.ContactResponse;
import com.backend.c4s.Entity.Contact;
import com.backend.c4s.Mapper.ContactMapper;
import com.backend.c4s.Repository.ContactRepository;
import com.backend.c4s.Service.ContactService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    @Override
    @Transactional
    public ContactResponse createContact(ContactRequest request) {
        Contact contact = contactMapper.toContact(request);
        Contact savedContact = contactRepository.save(contact);
        return contactMapper.toContactResponse(savedContact);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactResponse getContactById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact message not found with ID: " + id));
        return contactMapper.toContactResponse(contact);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactResponse> getAlContact(Pageable pageable) {
        return contactRepository.findAll(pageable)
                .map(contactMapper::toContactResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContactResponse> getContactByStatus(Boolean isResolved, Pageable pageable) {
        return contactRepository.findByIsResolved(isResolved, pageable)
                .map(contactMapper::toContactResponse);
    }

    @Override
    @Transactional
    public ContactResponse toggleResolveStatus(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Contact message not found with ID: " + id));

        contact.setIsResolved(!contact.getIsResolved());
        Contact updatedContact = contactRepository.save(contact);
        return contactMapper.toContactResponse(updatedContact);
    }

    @Override
    @Transactional
    public void delectContact(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new EntityNotFoundException("Contact message not found with ID: " + id);
        }
        contactRepository.deleteById(id);
    }
}
