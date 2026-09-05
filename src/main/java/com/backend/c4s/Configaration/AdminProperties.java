package com.backend.c4s.Configaration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.admin")
public class AdminProperties {
    private String email = "admin@company.com";
    private String password = "AdminSecret123!";
    private String firstName = "System";
    private String lastName = "Admin";
}
