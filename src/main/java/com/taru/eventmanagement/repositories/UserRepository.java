package com.taru.eventmanagement.repositories;

import com.taru.eventmanagement.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
