package com.taru.eventmanagement.repositories;

import com.taru.eventmanagement.models.EventAttendee;
import com.taru.eventmanagement.models.EventAttendeeId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventAttendeeRepository extends JpaRepository<EventAttendee, EventAttendeeId> {

    Page<EventAttendee> findByAttendeeUserId(int attendeeId, Pageable pageable);
    List<EventAttendee> findByAttendeeUserId(int attendeeId);
    List<EventAttendee> findByEventEventIdAndStatus(int eventId, String status);
    boolean existsById(EventAttendeeId eventAttendeeId);
    int countByAttendeeUserId(int userId);
}
