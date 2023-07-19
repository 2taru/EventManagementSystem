package com.taru.eventmanagement.controllers;

import com.taru.eventmanagement.dto.EventDTO;
import com.taru.eventmanagement.services.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/event")
    public String listEvents(Model model){

        List<EventDTO> events = eventService.getAllEvents();

        model.addAttribute("events", events);

        return "list-events";
    }

    @GetMapping("/event/create")
    public String createEventForm(Model model){

        model.addAttribute("event", new EventDTO());
        return "event-create";
    }

    @PostMapping("/event/create")
    public String createEvent(@ModelAttribute("event") EventDTO event){

        eventService.createEvent(event);

        return "redirect:/event";
    }

    @GetMapping("/event/{eventId}/edit")
    public String updateEventForm(@PathVariable("eventId") int eventId, Model model){

        EventDTO event = eventService.getEventById(eventId);

        model.addAttribute("event", event);
        return "event-edit";
    }

    @PostMapping("/event/{eventId}/edit")
    public String editEvent(@PathVariable("eventId") int eventId, @ModelAttribute("event") EventDTO event){

        event.setEventId(eventId);
        eventService.updateEventById(eventId, event);

        return "redirect:/event";
    }

    @GetMapping("/event/{eventId}")
    public String eventDetails(@PathVariable("eventId") int eventId, Model model){

        EventDTO event = eventService.getEventById(eventId);

        model.addAttribute("event", event);

        return "event-detail";
    }

    @GetMapping("/event/{eventId}/delete")
    public String deleteEvent(@PathVariable("eventId") int eventId){

        eventService.deleteEventById(eventId);

        return "redirect:/event";
    }
}
