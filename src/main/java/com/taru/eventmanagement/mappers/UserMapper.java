package com.taru.eventmanagement.mappers;

import com.taru.eventmanagement.dto.UserDTO;
import com.taru.eventmanagement.models.User;

public class UserMapper {

    public static UserDTO mapToDto(User user) {

        return UserDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .registrationDate(user.getRegistrationDate())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .photoUrl(user.getPhotoUrl())
                .build();
    }

    public static User mapToEntity(UserDTO userDTO) {

        return User.builder()
                .userId(userDTO.getUserId())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .username(userDTO.getUsername())
                .registrationDate(userDTO.getRegistrationDate())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .photoUrl(userDTO.getPhotoUrl())
                .build();
    }
}