package it.dst.garage.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupDto(
        @Email(message = "Invalid email format") String email,
        @NotBlank(message = "Username cannot be blank") String username,
        @NotBlank(message = "Password cannot be blank") @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{6,72}$", message = "Password must contain at least one letter and one number and be between 6 and 72 characters") String password) {
}
