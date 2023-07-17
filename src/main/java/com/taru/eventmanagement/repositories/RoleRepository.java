package com.taru.eventmanagement.repositories;

import com.taru.eventmanagement.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
