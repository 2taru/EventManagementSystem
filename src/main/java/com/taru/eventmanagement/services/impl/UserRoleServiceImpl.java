package com.taru.eventmanagement.services.impl;

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
                .orElseThrow(() -> new /*UserNotFoundException*/RuntimeException("User with id = " + userId + " - not found!"));

        Role role = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new /*RoleNotFoundException*/RuntimeException("Role = \"ROLE_USER\" - not found!"));

        if (role.getName().equals("ROLE_ADMIN")){
            throw new /*AccessDeniedException*/RuntimeException("You can't create user_role with role ADMIN!");
        } else if (userRoleRepository.existsByUserId(userId)) {
            throw new /*UserRoleException*/RuntimeException("User with id = " + userId + " already have Role!");
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

        UserRole userRole = userRoleRepository.findByUserId(userId)
                .orElseThrow(() -> new /*UserRoleException*/RuntimeException("User with id = " + userId + " - don't have a role!"));
        if (userRole.getRole().getName().equals("ROLE_ADMIN")){
            throw new /*AccessDeniedException*/RuntimeException("You can't delete role of user with role ADMIN!");
        }
        userRoleRepository.deleteById(new UserRoleId(userId, userRole.getRole().getRoleId()));
    }
}