package com.taru.eventmanagement.services.impl;

import com.taru.eventmanagement.dto.EventDTO;
import com.taru.eventmanagement.mappers.EventMapper;
import com.taru.eventmanagement.models.Event;
import com.taru.eventmanagement.repositories.EventRepository;
import com.taru.eventmanagement.services.EventService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public EventDTO createEvent(EventDTO eventDTO) {

        Event event = EventMapper.mapToEntity(eventDTO);

        event = eventRepository.save(event);

        return EventMapper.mapToDto(event);
    }

    @Override
    public EventDTO updateEventById(int eventId, EventDTO eventDTO) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new /*EventNotFoundException*/RuntimeException("Event with id = " + eventId + " - not found!"));

        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setDateTime(eventDTO.getDateTime());
        event.setMaxAttendees(eventDTO.getMaxAttendees());
        event.setCancelled(eventDTO.isCancelled());

        Event updatedEvent = eventRepository.save(event);

        return EventMapper.mapToDto(updatedEvent);
    }

    @Override
    public EventDTO getEventById(int eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new /*EventNotFoundException*/RuntimeException("Event with id = " + eventId + " - not found!"));

        return EventMapper.mapToDto(event);
    }

    @Override
    public List<EventDTO> getAllEvents() {

        List<Event> events = eventRepository.findAll();

        return events.stream()
                .map(EventMapper::mapToDto)
                .toList();
    }

    @Override
    public void deleteEventById(int eventId) {

        eventRepository.deleteById(eventId);
    }
}