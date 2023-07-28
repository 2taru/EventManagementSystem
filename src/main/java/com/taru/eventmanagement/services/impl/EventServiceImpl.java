package com.taru.eventmanagement.services.impl;

import com.taru.eventmanagement.config.SecurityUtil;
import com.taru.eventmanagement.dto.EventDTO;
import com.taru.eventmanagement.dto.EventResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
        eventDTO.setDescription(eventDTO.getDescription().isEmpty() || eventDTO.getDescription().isBlank()
                ? "No description." : eventDTO.getDescription());

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
    public void changeStatus(int eventId, boolean isCanceled) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new MyNotFoundException("Event with id = " + eventId + " - not found!"));

        event.setCancelled(isCanceled);

        eventRepository.save(event);
    }

    @Override
    public EventDTO getEventById(int eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new MyNotFoundException("Event with id = " + eventId + " - not found!"));

        return EventMapper.mapToDto(event);
    }

    @Override
    public EventResponse getAllEvents(int pageNo, int pageSize, String sortBy, String sortType) {

        Page<Event> events = eventRepository.findAll(PageRequest.of(
                pageNo, pageSize, Sort.by(sortType.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy)));
        List<EventDTO> content = events.getContent().stream().map(EventMapper::mapToDto).toList();

        return EventResponse.builder()
                .content(content)
                .pageNo(events.getNumber())
                .pageSize(events.getSize())
                .totalElements(events.getTotalElements())
                .totalPages(events.getTotalPages())
                .last(events.isLast())
                .build();
    }

    @Override
    public EventResponse getAllEventsByCreatorId(int creatorId, int pageNo, int pageSize, String sortBy, String sortType) {

        Page<Event> events = eventRepository.findEventByCreatorUserId(creatorId, PageRequest.of(
                pageNo, pageSize, Sort.by(sortType.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy)));
        List<EventDTO> content = events.getContent().stream().map(EventMapper::mapToDto).toList();

        return EventResponse.builder()
                .content(content)
                .pageNo(events.getNumber())
                .pageSize(events.getSize())
                .totalElements(events.getTotalElements())
                .totalPages(events.getTotalPages())
                .last(events.isLast())
                .build();
    }

    @Override
    public EventResponse searchEvents(String query, int pageNo, int pageSize, String sortBy, String sortType) {

        Page<Event> events = eventRepository.searchEvents(query, PageRequest.of(
                pageNo, pageSize, Sort.by(sortType.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy)));
        List<EventDTO> content = events.getContent().stream().map(EventMapper::mapToDto).toList();

        return EventResponse.builder()
                .content(content)
                .pageNo(events.getNumber())
                .pageSize(events.getSize())
                .totalElements(events.getTotalElements())
                .totalPages(events.getTotalPages())
                .last(events.isLast())
                .build();
    }

    @Override
    public void deleteEventById(int eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new MyNotFoundException("Event with id = " + eventId + " - not found!"));

        String sessionUsername = SecurityUtil.getSessionUser();
        UserDTO sessionUser = userService.getUserByUsername(sessionUsername);
        UserRole sessionUserRole = userRoleRepository.findByUserUserId(sessionUser.getUserId())
                .orElseThrow(() -> new MyNotFoundException("User with username = " + sessionUser.getUserId() + " - not found!"));

        if (sessionUser.getUserId() != event.getCreator().getUserId() && !sessionUserRole.getRole().getName().equals("ROLE_ADMIN")) {
            throw new AccessDeniedException("You do not have rights to delete Events that you did not create.");
        }

        eventRepository.deleteById(eventId);
    }
}
