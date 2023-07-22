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
public class EventAttendeeDTO {

    private int eventAttendeeId;
    private EventDTO event;
    private UserDTO attendee;
    private LocalDateTime registrationDate;
    private String status;
}