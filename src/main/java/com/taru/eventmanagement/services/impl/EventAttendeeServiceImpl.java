package com.taru.eventmanagement.services.impl;

import com.taru.eventmanagement.dto.EventAttendeeDTO;
import com.taru.eventmanagement.dto.EventDTO;
import com.taru.eventmanagement.mappers.EventAttendeeMapper;
import com.taru.eventmanagement.mappers.EventMapper;
import com.taru.eventmanagement.mappers.UserMapper;
import com.taru.eventmanagement.models.Event;
import com.taru.eventmanagement.models.EventAttendee;
import com.taru.eventmanagement.models.User;
import com.taru.eventmanagement.repositories.EventAttendeeRepository;
import com.taru.eventmanagement.repositories.EventRepository;
import com.taru.eventmanagement.repositories.UserRepository;
import com.taru.eventmanagement.services.EventAttendeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventAttendeeServiceImpl implements EventAttendeeService {

    private final EventAttendeeRepository eventAttendeeRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public EventAttendeeServiceImpl(EventAttendeeRepository eventAttendeeRepository, UserRepository userRepository, EventRepository eventRepository) {
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public EventAttendeeDTO createEventAttendee(int eventId, int attendeeId, EventAttendeeDTO eventAttendeeDTO) {

        User user = userRepository.findById(attendeeId)
                .orElseThrow(() -> new /*UserNotFoundException*/RuntimeException("User with id = " + attendeeId + " - not found!"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new /*EventNotFoundException*/RuntimeException("Event with id = " + eventId + " - not found!"));

        eventAttendeeDTO.setAttendee(UserMapper.mapToDto(user));
        eventAttendeeDTO.setEvent(EventMapper.mapToDto(event));

        EventAttendee eventAttendee = EventAttendeeMapper.mapToEntity(eventAttendeeDTO);

        EventAttendee updatedEventAttendee = eventAttendeeRepository.save(eventAttendee);

        return EventAttendeeMapper.mapToDto(updatedEventAttendee);
    }

    @Override
    public EventAttendeeDTO updateEventAttendeeStatusById(int eventAttendeeId, EventAttendeeDTO eventDTO) {

        EventAttendee eventAttendee = eventAttendeeRepository.findById(eventAttendeeId)
                .orElseThrow(() -> new /*EventAttendeeNotFoundException*/RuntimeException("EventAttendee with id = " + eventAttendeeId + " - not found!"));

        eventAttendee.setStatus(eventDTO.getStatus());

        EventAttendee updatedEventAttendee = eventAttendeeRepository.save(eventAttendee);

        return EventAttendeeMapper.mapToDto(updatedEventAttendee);
    }

    @Override
    public EventAttendeeDTO getEventAttendeeById(int eventAttendeeId) {

        EventAttendee eventAttendee = eventAttendeeRepository.findById(eventAttendeeId)
                .orElseThrow(() -> new /*EventAttendeeNotFoundException*/RuntimeException("EventAttendee with id = " + eventAttendeeId + " - not found!"));

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
    public List<EventDTO> getAllEventsByAttendeeId(int attendeeId) {

        List<EventAttendee> eventAttendees = eventAttendeeRepository.findByAttendeeUserId(attendeeId);

        return eventAttendees.stream()
                .map(ea -> EventMapper.mapToDto(ea.getEvent()))
                .toList();
    }

    @Override
    public List<EventAttendeeDTO> getAllEventAttendeesByEventId(int eventId) {

        List<EventAttendee> eventAttendees = eventAttendeeRepository.findByEventEventId(eventId);

        return eventAttendees.stream()
                .map(EventAttendeeMapper::mapToDto)
                .toList();
    }

    @Override
    public void deleteEventAttendeeById(int eventAttendeeId) {

        eventAttendeeRepository.deleteById(eventAttendeeId);
    }
}
