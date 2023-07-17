package com.taru.eventmanagement.dto;

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
    private String username;
    private String email;
    private String password;
    private LocalDateTime registrationDate;
    private RoleDTO role;
}