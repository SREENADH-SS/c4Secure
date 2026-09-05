package com.backend.c4s.Configaration;

import com.backend.c4s.Entity.Users;
import com.backend.c4s.Entity.common.Role;
import com.backend.c4s.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminProperties adminProperties;


    @Override
    public void run(String... args)  {

        String cleanEmail = adminProperties.getEmail().trim().toLowerCase();

        if (!userRepository.existsByEmail(cleanEmail)){
            Users admin= Users.builder()
                    .firstName(adminProperties.getFirstName())
                    .lastName(adminProperties.getLastName())
                    .email(cleanEmail)
                    .password(passwordEncoder.encode(adminProperties.getPassword()))
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            log.info("Default Admin account initialized successfully for email: {}", cleanEmail);
        }

    }
}
