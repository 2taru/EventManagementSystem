package com.taru.eventmanagement.services;

import com.taru.eventmanagement.dto.UserDTO;

import java.util.List;

public interface UserService {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    UserDTO createUser(UserDTO user);

    UserDTO updateUserById(int userId, UserDTO user);

    UserDTO getUserById(int userId);

    UserDTO getUserByUsername(String username);

    UserDTO getUserByEmail(String email);

    List<UserDTO> getAllUsers();

    void deleteUserById(int userId);
}