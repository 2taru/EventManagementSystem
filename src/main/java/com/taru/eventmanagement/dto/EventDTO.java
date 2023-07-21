package com.taru.eventmanagement.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

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