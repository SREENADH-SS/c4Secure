package com.backend.c4s.Controller;

import com.backend.c4s.Dto.Contact.ContactRequest;
import com.backend.c4s.Dto.Contact.ContactResponse;
import com.backend.c4s.Service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contacts")
@RequiredArgsConstructor
@Tag(name = "Contact", description = "an Endpoint for the contact")
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    @Operation(summary = "To create contact for public")
    public ResponseEntity<ContactResponse>createContact(@Valid @RequestBody ContactRequest request){
        ContactResponse response= contactService.createContact(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "To get all contact details to admin")
    public ResponseEntity<Page<ContactResponse>> getAllContacts(
            @RequestParam(required = false) Boolean resolved,
            @PageableDefault(size = 10, sort = "createdAt",direction = Sort.Direction.DESC)Pageable pageable
            ){
        Page<ContactResponse> contacts;

        if (resolved!=null){
            contacts= contactService.getContactByStatus(resolved, pageable);
        }
        else {
            contacts = contactService.getAlContact(pageable);
        }
        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "To get the Contact Details by id")
    public ResponseEntity<ContactResponse> getContactById(@PathVariable Long id){
        return ResponseEntity.ok(contactService.getContactById(id));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "to mark when problem of the user if solved ")
    public ResponseEntity<ContactResponse> toggleResolvedStatus(@PathVariable Long id){
        return ResponseEntity.ok(contactService.toggleResolveStatus(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "To delect the Contact ")
    public ResponseEntity<Void> delectContact(@PathVariable Long id){
        contactService.delectContact(id);
        return ResponseEntity.noContent().build();
    }
}
