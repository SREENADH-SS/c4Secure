package com.backend.c4s.Service;

import com.backend.c4s.Dto.User.*;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse getUserById(Long id);

    AdminUserResponse getAdminUserById(Long Id);

    UserResponse getUserEmail(String email);

    List<UserResponse>getAllUser();

    List<AdminUserResponse>etAllUsersForAdmin();

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void changePassword(Long id, ChangePasswordRequest request);

    void deleteUser(Long id);
}
