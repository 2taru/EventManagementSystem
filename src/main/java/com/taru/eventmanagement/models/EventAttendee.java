package com.taru.eventmanagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "event_attendees")
public class EventAttendee {

    @EmbeddedId
    private EventAttendeeId id;
    @ManyToOne
    @JoinColumn(name = "event_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fc_event_if_events"))
    private Event event;
    @ManyToOne
    @JoinColumn(name = "attendee_id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fc_attendee_id_users"))
    private User attendee;
    @Column(name = "registration_date")
    @CreationTimestamp
    private LocalDateTime registrationDate;
    @Column(name = "status", length = 45)
    private String status;
}