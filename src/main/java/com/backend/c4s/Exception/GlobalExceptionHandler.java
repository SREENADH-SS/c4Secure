package com.backend.c4s.Exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handle Entity/Resource Not Found (404)
    @ExceptionHandler({EntityNotFoundException.class, ResourceNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleNotFound(Exception ex) {
        return build(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    // 2. Handle Custom Bad Request & Illegal State (400)
    @ExceptionHandler({BadRequestException.class, IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<Map<String, Object>> handleBadRequest(Exception ex) {
        return build(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
    }

    // 3. Handle @Valid Validation Failures in Request Bodies (400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Failed");
        body.put("errors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // 4. Handle Database Constraint Violations (409 Conflict)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT, "Database Conflict", "A record with this information already exists or violates database constraints.");
    }

    // 5. Handle Security Authorization / Access Denied (403)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "Forbidden", "You do not have permission to access this resource.");
    }

    // 6. Handle Security Bad Credentials (401)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Unauthorized", "Invalid email or password.");
    }

    // 7. Fallback Handler for unexpected exceptions (500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "An unexpected error occurred: " + ex.getMessage());
    }

    // Helper method to maintain consistent JSON structure
    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String error, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
