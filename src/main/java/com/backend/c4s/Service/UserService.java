package com.backend.c4s.Service;

import com.backend.c4s.Dto.User.AdminUserResponse;
import com.backend.c4s.Dto.User.ChangePasswordRequest;
import com.backend.c4s.Dto.User.UserRequest;
import com.backend.c4s.Dto.User.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse getUserById(Long id);

    AdminUserResponse getAdminUserById(Long Id);

    UserResponse getUserEmail(String email);

    List<UserResponse>getAllUser();

    List<AdminUserResponse>etAllUsersForAdmin();

    UserResponse updateUser(Long id, UserRequest request);

    void changePassword(Long id, ChangePasswordRequest request);

    void deleteUser(Long id);
}
