package com.taru.eventmanagement.controllers;

import com.taru.eventmanagement.dto.EventDTO;
import com.taru.eventmanagement.services.EventService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
    public String createEvent(Model model){

        model.addAttribute("event", new EventDTO());
        return "event-create";
    }

    @PostMapping("/event")
    public String createEvent(@ModelAttribute("event") EventDTO event){

        eventService.createEvent(event);

        return "redirect:/event";
    }
}
