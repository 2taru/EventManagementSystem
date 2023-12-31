package com.taru.eventmanagement.services.impl;

import com.taru.eventmanagement.config.SecurityUtil;
import com.taru.eventmanagement.dto.UserDTO;
import com.taru.eventmanagement.exception.AccessDeniedException;
import com.taru.eventmanagement.exception.MyNotFoundException;
import com.taru.eventmanagement.mappers.UserMapper;
import com.taru.eventmanagement.models.User;
import com.taru.eventmanagement.models.UserRole;
import com.taru.eventmanagement.repositories.UserRepository;
import com.taru.eventmanagement.repositories.UserRoleRepository;
import com.taru.eventmanagement.services.RoleService;
import com.taru.eventmanagement.services.UserRoleService;
import com.taru.eventmanagement.services.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final RoleService roleService;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            UserRoleService userRoleService,
            RoleService roleService,
            UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
        this.roleService = roleService;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Boolean existsByUsername(String username) {

        return userRepository.existsByUsername(username);
    }

    @Override
    public Boolean existsByEmail(String email) {

        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {

        User user = UserMapper.mapToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);

        userRoleService.createUserRole(user.getUserId());

        UserDTO result = UserMapper.mapToDto(user);
        result.setRole(roleService.getRoleByUserId(user.getUserId()));

        return result;
    }

    @Override
    public UserDTO updateUserById(int userId, UserDTO userDTO) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new MyNotFoundException("User with id = " + userId + " - not found!"));

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhotoUrl(userDTO.getPhotoUrl());

        User updatedUser = userRepository.save(user);

        UserDTO result = UserMapper.mapToDto(updatedUser);
        result.setRole(roleService.getRoleByUserId(updatedUser.getUserId()));

        return result;
    }

    @Override
    public UserDTO getUserById(int userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new MyNotFoundException("User with id = " + userId + " - not found!"));

        UserDTO result = UserMapper.mapToDto(user);
        result.setRole(roleService.getRoleByUserId(user.getUserId()));

        return result;
    }

    @Override
    public UserDTO getUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new MyNotFoundException("User with username = " + username + " - not found!"));

        UserDTO result = UserMapper.mapToDto(user);
        result.setRole(roleService.getRoleByUserId(user.getUserId()));

        return result;
    }

    @Override
    public UserDTO getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new MyNotFoundException("User with email = " + email + " - not found!"));

        UserDTO result = UserMapper.mapToDto(user);
        result.setRole(roleService.getRoleByUserId(user.getUserId()));

        return result;
    }

    @Override
    public List<UserDTO> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(u -> {
                    UserDTO userDTO = UserMapper.mapToDto(u);
                    userDTO.setRole(roleService.getRoleByUserId(u.getUserId()));
                    return userDTO;
                }).toList();
    }

    @Override
    public void deleteUserById(int userId) {

        String sessionUsername = SecurityUtil.getSessionUser();
        User sessionUser = userRepository.findByUsername(sessionUsername)
                .orElseThrow(() -> new MyNotFoundException("User with username = " + sessionUsername + " - not found!"));
        UserRole sessionUserRole = userRoleRepository.findByUserUserId(sessionUser.getUserId())
                .orElseThrow(() -> new MyNotFoundException("User with username = " + sessionUser.getUserId() + " - not found!"));

        if (sessionUser.getUserId() != userId && !sessionUserRole.getRole().getName().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("You don't have rights to delete other Users.");
        }

        UserRole userRole = userRoleRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("User with id = " + userId + " - does not have a role!"));

        if (userRole.getRole().getName().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("You can't delete user with role ADMIN!");
        }

        userRoleService.deleteUserRoleByUserId(userId);
        userRepository.deleteById(userId);
    }
}