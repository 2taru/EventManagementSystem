package com.taru.eventmanagement.repositories;

import com.taru.eventmanagement.models.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("SELECT e from Event e WHERE e.name LIKE CONCAT('%', :query, '%')")
    Page<Event> searchEvents(String query, Pageable pageable);

    Page<Event> findEventByCreatorUserId(int creatorId, Pageable pageable);

    int countByCreatorUserId(int userId);
}
