package com.taru.eventmanagement.repositories;

import com.taru.eventmanagement.models.UserRole;
import com.taru.eventmanagement.models.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

    boolean existsByUserId(Integer userId);
    Optional<UserRole> findByUserId(Integer userId);
    void deleteById(UserRoleId userRoleId);
}
