package com.taru.eventmanagement.services;

import com.taru.eventmanagement.dto.EventDTO;

import java.util.List;

public interface EventService {

    EventDTO createEvent(EventDTO eventDTO);

    EventDTO updateEventById(int eventId, EventDTO eventDTO);

    EventDTO getEventById(int eventId);

    List<EventDTO> getAllEvents();

    List<EventDTO> searchEvents(String query);

    void deleteEventById(int eventId);
}
