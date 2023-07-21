package com.taru.eventmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private int userId;
    @NotBlank(message = "Username is mandatory!")
    private String username;
    @NotBlank(message = "Email is mandatory!")
    @Email(message = "Invalid email format!")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$", message = "Password is mandatory and must contain at least eight characters, at least one letter and one number!")
    private String password;
    private LocalDateTime registrationDate;
    private RoleDTO role;
}