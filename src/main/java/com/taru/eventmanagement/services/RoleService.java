package com.taru.eventmanagement.services;

import com.taru.eventmanagement.dto.RoleDTO;

import java.util.List;

public interface RoleService {

    RoleDTO getRoleById(int roleId);

    RoleDTO getRoleByUserId(int userId);

    List<RoleDTO> getAllRoles();
}