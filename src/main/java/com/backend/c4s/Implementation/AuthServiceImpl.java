package com.backend.c4s.Implementation;

import com.backend.c4s.Configaration.JwtService;
import com.backend.c4s.Dto.Auth.AuthResponse;
import com.backend.c4s.Dto.Auth.LoginRequest;
import com.backend.c4s.Dto.Auth.RegisterRequest;
import com.backend.c4s.Entity.Users;
import com.backend.c4s.Entity.common.Role;
import com.backend.c4s.Exception.BadRequestException;
import com.backend.c4s.Exception.DuplicateResourceException;
import com.backend.c4s.Mapper.UserMapper;
import com.backend.c4s.Repository.UserRepository;
import com.backend.c4s.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;


    @Override
    @SuppressWarnings("DataFlowIssue")
    public AuthResponse login(LoginRequest request) {
        String cleanEmail= request.getEmail().trim().toLowerCase();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(cleanEmail, request.getPassword())
            );

            Users user = (Users) authentication.getPrincipal();

            Long userId = user.getId();
            if (userId == null) {
                throw new IllegalStateException("User ID cannot be null after authentication");
            }

            String token = jwtService.generateToken(user);

            return AuthResponse.builder()
                    .token(token)
                    .tokenType("Bearer")
                    .id(userId)
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .role(user.getRole())
                    .user(userMapper.toResponse((user)))
                    .build();
        }
        catch (BadCredentialsException ex){
            throw new BadRequestException("Invalid email or password");
        }
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        String cleanEmail = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(cleanEmail)){
            throw new DuplicateResourceException("Users", "email", cleanEmail);
        }

        Users user =Users.builder()
                .firstName(request.getFirstName() != null ? request.getFirstName().trim() : null )
                .lastName(request.getLastName() != null ? request.getLastName().trim() : null)
                .email(cleanEmail)
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone()!= null? request.getPhone().trim() : null)
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .role(request.getRole()!= null ? request.getRole() : Role.USER)
                .build();

        userRepository.save(user);

        return login(new LoginRequest(cleanEmail, request.getPassword()));
    }
}
