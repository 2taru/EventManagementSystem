package com.taru.eventmanagement.services.impl;

import com.taru.eventmanagement.config.SecurityUtil;
import com.taru.eventmanagement.dto.EventAttendeeDTO;
import com.taru.eventmanagement.dto.EventDTO;
import com.taru.eventmanagement.dto.EventResponse;
import com.taru.eventmanagement.dto.UserDTO;
import com.taru.eventmanagement.exception.AccessDeniedException;
import com.taru.eventmanagement.exception.MyNotFoundException;
import com.taru.eventmanagement.mappers.EventAttendeeMapper;
import com.taru.eventmanagement.mappers.EventMapper;
import com.taru.eventmanagement.models.Event;
import com.taru.eventmanagement.models.EventAttendee;
import com.taru.eventmanagement.models.EventAttendeeId;
import com.taru.eventmanagement.repositories.EventAttendeeRepository;
import com.taru.eventmanagement.repositories.EventRepository;
import com.taru.eventmanagement.services.EventAttendeeService;
import com.taru.eventmanagement.services.EventService;
import com.taru.eventmanagement.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventAttendeeServiceImpl implements EventAttendeeService {

    private final EventAttendeeRepository eventAttendeeRepository;
    private final UserService userService;
    private final EventService eventService;
    private final EventRepository eventRepository;

    public EventAttendeeServiceImpl(
            EventAttendeeRepository eventAttendeeRepository,
            UserService userService,
            EventService eventService,
            EventRepository eventRepository
    ) {
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.userService = userService;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }

    @Override
    public EventAttendeeDTO createEventAttendee(int eventId, EventAttendeeDTO eventAttendeeDTO) {

        String username = SecurityUtil.getSessionUser();
        UserDTO sessionUser = userService.getUserByUsername(username);
        EventDTO event = eventService.getEventById(eventId);

        if (sessionUser.getUserId() == event.getCreator().getUserId()) {
            throw new AccessDeniedException("You can't attend to Event that you create.");
        }
        if (event.getCurrentAttendees() + 1 > event.getMaxAttendees()) {
            throw new AccessDeniedException("You can't attend to this Event! It already have maximum attendees.");
        }

        eventAttendeeDTO.setAttendee(sessionUser);
        eventAttendeeDTO.setEvent(event);

        EventAttendee eventAttendee = EventAttendeeMapper.mapToEntity(eventAttendeeDTO);

        eventAttendee.setId(new EventAttendeeId(event.getEventId(), sessionUser.getUserId()));
        EventAttendee updatedEventAttendee = eventAttendeeRepository.save(eventAttendee);
        event.setCurrentAttendees(event.getCurrentAttendees() + 1);
        eventRepository.save(EventMapper.mapToEntity(event));

        return EventAttendeeMapper.mapToDto(updatedEventAttendee);
    }

    @Override
    public EventAttendeeDTO updateEventAttendeeStatusById(EventAttendeeId eventAttendeeId, EventAttendeeDTO eventDTO) {

        EventAttendee eventAttendee = eventAttendeeRepository.findById(eventAttendeeId)
                .orElseThrow(() -> new MyNotFoundException("EventAttendee with id = " + eventAttendeeId + " - not found!"));

        eventAttendee.setStatus(eventDTO.getStatus());

        EventAttendee updatedEventAttendee = eventAttendeeRepository.save(eventAttendee);

        return EventAttendeeMapper.mapToDto(updatedEventAttendee);
    }

    @Override
    public EventAttendeeDTO getEventAttendeeById(EventAttendeeId eventAttendeeId) {

        EventAttendee eventAttendee = eventAttendeeRepository.findById(eventAttendeeId)
                .orElseThrow(() -> new MyNotFoundException("EventAttendee with id = " + eventAttendeeId + " - not found!"));

        return EventAttendeeMapper.mapToDto(eventAttendee);
    }

    @Override
    public List<EventAttendeeDTO> getAllEventAttendeesByAttendeeId(int attendeeId) {

        List<EventAttendee> eventAttendees = eventAttendeeRepository.findByAttendeeUserId(attendeeId);

        return eventAttendees.stream()
                .map(EventAttendeeMapper::mapToDto)
                .toList();
    }

    @Override
    public EventResponse getAllEventsByAttendeeId(int attendeeId, int pageNo, int pageSize, String sortBy, String sortType) {

        Page<EventAttendee> eventAttendees = eventAttendeeRepository.findByAttendeeUserId(attendeeId, PageRequest.of(
                pageNo, pageSize, Sort.by(sortType.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy)));
        List<EventDTO> content = eventAttendees.getContent().stream().map(ea -> EventMapper.mapToDto(ea.getEvent())).toList();

        return EventResponse.builder()
                .content(content)
                .pageNo(eventAttendees.getNumber())
                .pageSize(eventAttendees.getSize())
                .totalElements(eventAttendees.getTotalElements())
                .totalPages(eventAttendees.getTotalPages())
                .last(eventAttendees.isLast())
                .build();
    }

    @Override
    public List<EventAttendeeDTO> getAllEventAttendeesByEventCreatorId(int userId) {

        List<Event> events = eventRepository.findEventByCreatorUserId(userId);

        List<EventAttendee> eventAttendees = new ArrayList<>();

        events.forEach(e -> eventAttendees.addAll(eventAttendeeRepository.findByEventEventIdAndStatus(e.getEventId(), "Attended")));

        return eventAttendees.stream()
                .map(EventAttendeeMapper::mapToDto)
                .toList();
    }

    @Override
    public void deleteEventAttendeeByEventId(int eventId) {

        String username = SecurityUtil.getSessionUser();
        UserDTO userDTO = userService.getUserByUsername(username);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new MyNotFoundException("Event with id = " + eventId + " - not found!"));

        eventAttendeeRepository.deleteById(new EventAttendeeId(eventId, userDTO.getUserId()));

        event.setCurrentAttendees(event.getCurrentAttendees() - 1);
        eventRepository.save(event);
    }

    @Override
    public void approveAttendee(EventAttendeeId eventAttendeeId) {

        EventAttendee eventAttendee = eventAttendeeRepository.findById(eventAttendeeId)
                .orElseThrow(() -> new MyNotFoundException("EventAttendee with id = " + eventAttendeeId + " - not found!"));

        eventAttendee.setStatus("Approved");

        eventAttendeeRepository.save(eventAttendee);
    }

    @Override
    public void disapproveAttendee(EventAttendeeId eventAttendeeId) {

        EventAttendee eventAttendee = eventAttendeeRepository.findById(eventAttendeeId)
                .orElseThrow(() -> new MyNotFoundException("EventAttendee with id = " + eventAttendeeId + " - not found!"));

        eventAttendee.setStatus("Disapproved");

        eventAttendeeRepository.save(eventAttendee);
    }
}
