package com.backend.c4s.Implementation;

import com.backend.c4s.Dto.User.AdminUserResponse;
import com.backend.c4s.Dto.User.ChangePasswordRequest;
import com.backend.c4s.Dto.User.UserRequest;
import com.backend.c4s.Dto.User.UserResponse;
import com.backend.c4s.Entity.Users;
import com.backend.c4s.Entity.common.Role;
import com.backend.c4s.Exception.DuplicateResourceException;
import com.backend.c4s.Exception.ResourceNotFoundException;
import com.backend.c4s.Mapper.UserMapper;
import com.backend.c4s.Repository.UserRepository;
import com.backend.c4s.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail((request.getEmail()).trim().toLowerCase())){
            throw new DuplicateResourceException("User", "Email", request.getEmail());
        }
        Users user=userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassWord()));
        user.setRole(Role.USER);

        Users savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);

    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        Users user = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Users", "id", id));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUserResponse getAdminUserById(Long id) {
        Users user =userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Users", "id", id));
        return userMapper.toAdminUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserEmail(String email) {
        Users user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(()-> new ResourceNotFoundException("Users", "email", email));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUser() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminUserResponse> etAllUsersForAdmin() {
        return userRepository.findAll().stream()
                .map(userMapper::toAdminUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        Users existingUser = userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Users", "id", id));

        existingUser.setFirstName(request.getFirstName()!=null ? request.getFirstName().trim(): null);
        existingUser.setLastName(request.getLastName()!=null ? request.getLastName().trim(): null);
        existingUser.setPhone((request.getPhone()!=null ? request.getPhone().trim(): null));
        existingUser.setCity(request.getCity());
        existingUser.setState(request.getState());
        existingUser.setPostalCode(request.getPostalCode());

        if(request.getPassWord()!=null && !request.getPassWord().isBlank()){
            existingUser.setPassword(passwordEncoder.encode(request.getPassWord()));
        }
        Users updatedUser = userRepository.save(existingUser);
        return userMapper.toResponse(updatedUser);
    }

    @Override
    public void changePassword(Long id, ChangePasswordRequest request) {
        Users user=  userRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Users", "id", id));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPassword())){
            throw new IllegalArgumentException("Current password does not match.");
        }
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)){
            throw new ResourceNotFoundException("Users", "id", id);
        }
        userRepository.deleteById(id);
    }
}
