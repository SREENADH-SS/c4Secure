package com.backend.c4s.Dto.Contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ContactResponse {
    private Long id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private Boolean isResolved;
    private LocalDateTime createdAt;
}
