package com.backend.c4s.Service;

import com.backend.c4s.Dto.Auth.AuthResponse;
import com.backend.c4s.Dto.Auth.LoginRequest;
import com.backend.c4s.Dto.Auth.RegisterRequest;

public interface AuthService {

      AuthResponse login(LoginRequest request);

      AuthResponse register(RegisterRequest request);
}
