package com.taru.eventmanagement.repositories;

import com.taru.eventmanagement.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer> {
}
