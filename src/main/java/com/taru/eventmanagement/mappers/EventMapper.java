package com.taru.eventmanagement.mappers;

import com.taru.eventmanagement.dto.EventDTO;
import com.taru.eventmanagement.models.Event;

public class EventMapper {

    public static EventDTO mapToDto(Event event) {

        return EventDTO.builder()
                .eventId(event.getEventId())
                .name(event.getName())
                .description(event.getDescription())
                .photoUrl(event.getPhotoUrl())
                .dateTime(event.getDateTime())
                .maxAttendees(event.getMaxAttendees())
                .currentAttendees(event.getCurrentAttendees())
                .isCancelled(event.isCancelled())
                .creationDate(event.getCreationDate())
                .build();
    }

    public static Event mapToEntity(EventDTO eventDTO) {

        return Event.builder()
                .eventId(eventDTO.getEventId())
                .name(eventDTO.getName())
                .description(eventDTO.getDescription())
                .photoUrl(eventDTO.getPhotoUrl())
                .dateTime(eventDTO.getDateTime())
                .maxAttendees(eventDTO.getMaxAttendees())
                .currentAttendees(eventDTO.getCurrentAttendees())
                .isCancelled(eventDTO.isCancelled())
                .creationDate(eventDTO.getCreationDate())
                .build();
    }
}