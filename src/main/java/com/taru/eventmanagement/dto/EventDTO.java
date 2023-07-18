package com.taru.eventmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {

    private int eventId;
    private String name;
    private String description;
    private String photoUrl;
    private LocalDateTime dateTime;
    private int maxAttendees;
    private int currentAttendees;
    private boolean isCancelled;
    private LocalDateTime creationDate;
    private UserDTO creator;
}