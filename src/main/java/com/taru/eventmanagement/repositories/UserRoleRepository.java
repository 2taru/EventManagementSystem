package com.taru.eventmanagement.repositories;

import com.taru.eventmanagement.models.UserRole;
import com.taru.eventmanagement.models.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {
}
