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
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private int eventId;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "photo_url")
    private String photoUrl;
    @Column(name = "date_time")
    private LocalDateTime dateTime;
    @Column(name = "max_attendees")
    private int maxAttendees;
    @Column(name = "current_attendees")
    private int currentAttendees;
    @Column(name = "is_cancelled")
    private boolean isCancelled;
    @CreationTimestamp
    @Column(name = "creation_date", updatable = false)
    private LocalDateTime creationDate;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
}