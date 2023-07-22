package com.taru.eventmanagement.services;

import com.taru.eventmanagement.dto.EventAttendeeDTO;
import com.taru.eventmanagement.dto.EventDTO;

import java.util.List;

public interface EventAttendeeService {

    EventAttendeeDTO createEventAttendee(int eventId, int attendeeId, EventAttendeeDTO eventAttendeeDTO);

    EventAttendeeDTO updateEventAttendeeStatusById(int eventAttendeeId, EventAttendeeDTO eventDTO);

    EventAttendeeDTO getEventAttendeeById(int eventAttendeeId);

    List<EventAttendeeDTO> getAllEventAttendeesByAttendeeId(int attendeeId);

    List<EventDTO> getAllEventsByAttendeeId(int attendeeId);

    List<EventAttendeeDTO> getAllEventAttendeesByEventId(int eventId);

    void deleteEventAttendeeById(int eventAttendeeId);
}