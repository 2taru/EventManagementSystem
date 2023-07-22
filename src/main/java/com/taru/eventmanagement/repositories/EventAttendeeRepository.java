package com.taru.eventmanagement.repositories;

import com.taru.eventmanagement.models.Event;
import com.taru.eventmanagement.models.EventAttendee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventAttendeeRepository extends JpaRepository<EventAttendee, Integer> {

    List<EventAttendee> findByAttendeeUserId(int attendeeId);
    List<EventAttendee> findByEventEventId(int eventId);
}
