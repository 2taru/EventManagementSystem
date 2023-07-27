package com.taru.eventmanagement.services;

import com.taru.eventmanagement.dto.EventDTO;
import com.taru.eventmanagement.dto.EventResponse;

public interface EventService {

    EventDTO createEvent(EventDTO eventDTO);

    EventDTO updateEventById(int eventId, EventDTO eventDTO);

    void changeStatus(int eventId, boolean isCanceled);

    EventDTO getEventById(int eventId);

    EventResponse getAllEvents(int pageNo, int pageSize, String sortBy, String sortType);

    EventResponse getAllEventsByCreatorId(int creatorId, int pageNo, int pageSize, String sortBy, String sortType);

    EventResponse searchEvents(String query, int pageNo, int pageSize, String sortBy, String sortType);

    void deleteEventById(int eventId);
}
