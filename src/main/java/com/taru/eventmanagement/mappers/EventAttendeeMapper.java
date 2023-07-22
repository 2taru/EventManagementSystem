package com.taru.eventmanagement.mappers;

import com.taru.eventmanagement.dto.EventAttendeeDTO;
import com.taru.eventmanagement.models.EventAttendee;

public class EventAttendeeMapper {

    public static EventAttendeeDTO mapToDto(EventAttendee eventAttendee) {

        return EventAttendeeDTO.builder()
                .event(EventMapper.mapToDto(eventAttendee.getEvent()))
                .attendee(UserMapper.mapToDto(eventAttendee.getAttendee()))
                .registrationDate(eventAttendee.getRegistrationDate())
                .status(eventAttendee.getStatus())
                .build();
    }

    public static EventAttendee mapToEntity(EventAttendeeDTO eventAttendeeDTO) {

        return EventAttendee.builder()
                .event(EventMapper.mapToEntity(eventAttendeeDTO.getEvent()))
                .attendee(UserMapper.mapToEntity(eventAttendeeDTO.getAttendee()))
                .registrationDate(eventAttendeeDTO.getRegistrationDate())
                .status(eventAttendeeDTO.getStatus())
                .build();
    }
}