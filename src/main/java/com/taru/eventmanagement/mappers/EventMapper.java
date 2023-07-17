package com.taru.eventmanagement.mappers;

import com.taru.eventmanagement.dto.EventDTO;
import com.taru.eventmanagement.models.Event;

public class EventMapper {

    public static EventDTO mapToDto(Event event) {

        return EventDTO.builder()
                .eventId(event.getEventId())
                .name(event.getName())
                .description(event.getDescription())
                .dateTime(event.getDateTime())
                .maxAttendees(event.getMaxAttendees())
                .isCancelled(event.isCancelled())
                .creationDate(event.getCreationDate())
                .creator(UserMapper.mapToDto(event.getCreator()))
                .build();
    }

    public static Event mapToEntity(EventDTO eventDTO) {

        return Event.builder()
                .eventId(eventDTO.getEventId())
                .name(eventDTO.getName())
                .description(eventDTO.getDescription())
                .dateTime(eventDTO.getDateTime())
                .maxAttendees(eventDTO.getMaxAttendees())
                .isCancelled(eventDTO.isCancelled())
                .creationDate(eventDTO.getCreationDate())
                .creator(UserMapper.mapToEntity(eventDTO.getCreator()))
                .build();
    }
}