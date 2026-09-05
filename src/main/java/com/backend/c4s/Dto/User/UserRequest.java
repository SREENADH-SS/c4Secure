package com.backend.c4s.Dto.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Full Name is Required")
    @Size(max = 100, message = "Full Name Cannot Exceed 100 Characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @NotBlank(message = "Email is Required")
    @Email(message = "Invalid Email Formate")
    private String email;

    @NotBlank(message = "Phone Number is Required")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid Phone Number Formate")
    private String phone;

    @NotBlank(message = "Password is Required")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String passWord;

    private String address;
    private String city;
    private String state;
    private String postalCode;


}
