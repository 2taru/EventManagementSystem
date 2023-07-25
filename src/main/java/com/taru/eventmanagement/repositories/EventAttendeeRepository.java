package com.taru.eventmanagement.repositories;

import com.taru.eventmanagement.models.EventAttendee;
import com.taru.eventmanagement.models.EventAttendeeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventAttendeeRepository extends JpaRepository<EventAttendee, EventAttendeeId> {

    List<EventAttendee> findByAttendeeUserId(int attendeeId);
    List<EventAttendee> findByEventEventId(int eventId);
    boolean existsById(EventAttendeeId eventAttendeeId);
    int countByAttendeeUserId(int userId);
}
