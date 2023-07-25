package com.taru.eventmanagement.services.impl;

import com.taru.eventmanagement.config.SecurityUtil;
import com.taru.eventmanagement.dto.EventDTO;
import com.taru.eventmanagement.dto.UserDTO;
import com.taru.eventmanagement.exception.AccessDeniedException;
import com.taru.eventmanagement.exception.MyNotFoundException;
import com.taru.eventmanagement.mappers.EventMapper;
import com.taru.eventmanagement.models.Event;
import com.taru.eventmanagement.models.UserRole;
import com.taru.eventmanagement.repositories.EventRepository;
import com.taru.eventmanagement.repositories.UserRoleRepository;
import com.taru.eventmanagement.services.EventService;
import com.taru.eventmanagement.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserService userService;

    public EventServiceImpl(EventRepository eventRepository, UserRoleRepository userRoleRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.userRoleRepository = userRoleRepository;
        this.userService = userService;
    }

    @Override
    public EventDTO createEvent(EventDTO eventDTO) {

        eventDTO.setCreator(userService.getUserByUsername(SecurityUtil.getSessionUser()));
        eventDTO.setDescription(eventDTO.getDescription().isEmpty() || eventDTO.getDescription().isBlank() ? "No description." : eventDTO.getDescription());
        Event event = EventMapper.mapToEntity(eventDTO);

        event = eventRepository.save(event);

        return EventMapper.mapToDto(event);
    }

    @Override
    public EventDTO updateEventById(int eventId, EventDTO eventDTO) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new MyNotFoundException("Event with id = " + eventId + " - not found!"));

        event.setName(eventDTO.getName());
        event.setDescription(eventDTO.getDescription());
        event.setPhotoUrl(eventDTO.getPhotoUrl());
        event.setDateTime(eventDTO.getDateTime());
        event.setMaxAttendees(eventDTO.getMaxAttendees());
        event.setCancelled(eventDTO.isCancelled());

        Event updatedEvent = eventRepository.save(event);

        return EventMapper.mapToDto(updatedEvent);
    }

    @Override
    public EventDTO getEventById(int eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new MyNotFoundException("Event with id = " + eventId + " - not found!"));

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
    public List<EventDTO> getAllEventsByCreatorId(int creatorId) {

        List<Event> events = eventRepository.findEventByCreatorUserId(creatorId);

        return events.stream()
                .map(EventMapper::mapToDto)
                .toList();
    }

    @Override
    public List<EventDTO> searchEvents(String query) {

        List<Event> events = eventRepository.searchEvents(query);

        return events.stream()
                .map(EventMapper::mapToDto)
                .toList();
    }

    @Override
    public void deleteEventById(int eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new MyNotFoundException("Event with id = " + eventId + " - not found!"));

        String sessionUsername = SecurityUtil.getSessionUser();
        UserDTO sessionUser = userService.getUserByUsername(sessionUsername);
        UserRole sessionUserRole = userRoleRepository.findByUserUserId(sessionUser.getUserId())
                .orElseThrow(() -> new MyNotFoundException("User with username = " + sessionUser.getUserId() + " - not found!"));

        if (sessionUser.getUserId() != event.getCreator().getUserId() && !sessionUserRole.getRole().getName().equals("ROLE_ADMIN")){
            throw new AccessDeniedException("You do not have rights to delete Events that you did not create.");
        }

        eventRepository.deleteById(eventId);
    }
}
