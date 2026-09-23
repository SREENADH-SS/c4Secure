package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.Contact.ContactRequest;
import com.backend.c4s.Dto.Contact.ContactResponse;
import com.backend.c4s.Entity.Contact;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public Contact toContact(ContactRequest contactRequest){
        if(contactRequest== null){
            return null;
        }
        return Contact.builder()
                .name(contactRequest.getName())
                .email(contactRequest.getEmail())
                .subject(contactRequest.getSubject())
                .message(contactRequest.getMessage())
                .build();
    }

    public ContactResponse toContactResponse(Contact contactResponse){
        if(contactResponse==null){
            return null;
        }
        return ContactResponse.builder()
                .id(contactResponse.getId())
                .name(contactResponse.getName())
                .email(contactResponse.getEmail())
                .subject(contactResponse.getSubject())
                .message(contactResponse.getMessage())
                .isResolved(contactResponse.getIsResolved())
                .createdAt(contactResponse.getCreatedAt())
                .build();
    }

}
