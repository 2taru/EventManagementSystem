package com.taru.eventmanagement.services.impl;

import com.taru.eventmanagement.exception.AccessDeniedException;
import com.taru.eventmanagement.exception.MyNotFoundException;
import com.taru.eventmanagement.models.Role;
import com.taru.eventmanagement.models.User;
import com.taru.eventmanagement.models.UserRole;
import com.taru.eventmanagement.models.UserRoleId;
import com.taru.eventmanagement.repositories.RoleRepository;
import com.taru.eventmanagement.repositories.UserRepository;
import com.taru.eventmanagement.repositories.UserRoleRepository;
import com.taru.eventmanagement.services.UserRoleService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.userRoleRepository = userRoleRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void createUserRole(int userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new MyNotFoundException("User with id = " + userId + " - not found!"));

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new MyNotFoundException("Role = \"ROLE_USER\" - not found!"));

        if (role.getName().equals("ROLE_ADMIN")){
            throw new AccessDeniedException("You can't create user_role with role ADMIN!");
        } else if (userRoleRepository.existsByUserUserId(userId)) {
            throw new RuntimeException("User with id = " + userId + " already have Role!");
        }

        userRoleRepository.save(
                new UserRole(
                    new UserRoleId(
                        userId,
                        role.getRoleId()),
                    user,
                    role)
        );
    }

    @Override
    public void deleteUserRoleByUserId(int userId) {

        UserRole userRole = userRoleRepository.findByUserUserId(userId)
                .orElseThrow(() -> new MyNotFoundException("User with id = " + userId + " - don't have a role!"));
        if (userRole.getRole().getName().equals("ROLE_ADMIN")){
            throw new AccessDeniedException("You can't delete role of user with role ADMIN!");
        }
        userRoleRepository.deleteById(new UserRoleId(userId, userRole.getRole().getRoleId()));
    }
}