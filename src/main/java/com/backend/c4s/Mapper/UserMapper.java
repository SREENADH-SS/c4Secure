package com.backend.c4s.Mapper;

import com.backend.c4s.Dto.User.AdminUserResponse;
import com.backend.c4s.Dto.User.UserRequest;
import com.backend.c4s.Dto.User.UserResponse;
import com.backend.c4s.Entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public Users toEntity(UserRequest request){
        if(request ==null){
            return null;
        }
        return Users.builder()
                .firstName(request.getFirstName()!=null? request.getFirstName().trim():null)
                .lastName(request.getLastName()!=null? request.getLastName().trim():null)
                .email(request.getEmail()!=null? request.getEmail().trim().toLowerCase():null)
                .phone(request.getPhone()!=null? request.getPhone().trim():null)
                .address(request.getAddress())
                .city(request.getCity())
                .password(request.getPassWord())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .build();
    }
    public UserResponse toResponse(Users userDetails){
        if (userDetails==null){
            return null;
        }
        return UserResponse.builder()
                .id(userDetails.getId())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .email(userDetails.getEmail())
                .phone(userDetails.getPhone())
                .address(userDetails.getAddress())
                .city(userDetails.getCity())
                .state(userDetails.getState())
                .postalCode(userDetails.getPostalCode())
                .createdAt(userDetails.getCreatedAt())
                .updatedAt(userDetails.getUpdatedAt())
                .build();
    }

    public AdminUserResponse toAdminUserResponse(Users userDetails){
        if (userDetails== null){
            return null;
        }
        return AdminUserResponse.builder()
                .id(userDetails.getId())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .email(userDetails.getEmail())
                .phone(userDetails.getPhone())
                .role(userDetails.getRole())
                .address(userDetails.getAddress())
                .city(userDetails.getCity())
                .state(userDetails.getState())
                .postalCode(userDetails.getPostalCode())
                .enabled(userDetails.isEnabled())
                .accountNonLocked(userDetails.isAccountNonLocked())
                .createdAt(userDetails.getCreatedAt())
                .updatedAt(userDetails.getUpdatedAt())
                .build();
    }

    private String trim(String value){
        return value!=null? value.trim(): null;
    }
}