package com.taru.eventmanagement.mappers;

import com.taru.eventmanagement.dto.RoleDTO;
import com.taru.eventmanagement.models.Role;

public class RoleMapper {

    public static RoleDTO mapToDto(Role role) {

        return RoleDTO.builder()
                .roleId(role.getRoleId())
                .name(role.getName())
                .build();
    }

    public static Role mapToEntity(RoleDTO roleDTO) {

        return Role.builder()
                .roleId(roleDTO.getRoleId())
                .name(roleDTO.getName())
                .build();
    }
}