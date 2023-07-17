package com.taru.eventmanagement.services.impl;

import com.taru.eventmanagement.dto.RoleDTO;
import com.taru.eventmanagement.mappers.RoleMapper;
import com.taru.eventmanagement.models.Role;
import com.taru.eventmanagement.models.UserRole;
import com.taru.eventmanagement.repositories.RoleRepository;
import com.taru.eventmanagement.repositories.UserRoleRepository;
import com.taru.eventmanagement.services.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    public RoleServiceImpl(RoleRepository roleRepository, UserRoleRepository userRoleRepository) {

        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public RoleDTO getRoleById(int roleId) {

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new /*RoleNotFoundException*/RuntimeException("Role with id = " + roleId + " - not found!"));

        return RoleMapper.mapToDto(role);
    }

    @Override
    public RoleDTO getRoleByUserId(int userId) {

        UserRole userRole = userRoleRepository.findByUserUserId(userId)
                .orElseThrow(() -> new /*RoleNotFoundException*/RuntimeException("User with id = " + userId + " - don't have a role!"));

        return RoleMapper.mapToDto(userRole.getRole());
    }

    @Override
    public List<RoleDTO> getAllRoles() {

        List<Role> roles = roleRepository.findAll();

        return roles.stream()
                .map(RoleMapper::mapToDto)
                .toList();
    }
}