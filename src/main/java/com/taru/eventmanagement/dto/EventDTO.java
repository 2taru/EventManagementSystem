package com.taru.eventmanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Field is mandatory!")
    private String name;
    private String description;
    private String photoUrl;
    @NotNull(message = "Field is mandatory!")
    private LocalDateTime dateTime;
    @Min(value = 1, message = "Must be greater than 1")
    private int maxAttendees;
    private int currentAttendees;
    private boolean isCancelled;
    private LocalDateTime creationDate;
    private UserDTO creator;
}