package com.backend.c4s.Controller;

import com.backend.c4s.Dto.User.AdminUserResponse;
import com.backend.c4s.Dto.User.ChangePasswordRequest;
import com.backend.c4s.Dto.User.UserRequest;
import com.backend.c4s.Dto.User.UserResponse;
import com.backend.c4s.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for managing user accounts")

public class UserController {

    private final UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a user (Admin only)")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request){
        UserResponse response =userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get User Profile By Id")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get detailed user view including roles and account state (Admin only)")
    public ResponseEntity<AdminUserResponse> getAdminUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getAdminUserById(id));
    }

    @GetMapping("/email")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get User by Email")
    public ResponseEntity<UserResponse> getUserByEmail(@RequestParam String email){
        return ResponseEntity.ok(userService.getUserEmail(email));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users list (User view)")
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update user details")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request){
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete user by ID (Admin only)")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/change-password")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Change User Password")
    public ResponseEntity<Void>changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest request){
        userService.changePassword(id, request);
        return ResponseEntity.noContent().build();
    }
}
