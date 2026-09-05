package com.backend.c4s.Dto.Auth;

import com.backend.c4s.Dto.User.UserResponse;
import com.backend.c4s.Entity.common.Role;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

    private String token;

    @Builder.Default
    private String tokenType = "Bearer";

    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private UserResponse user;
}
