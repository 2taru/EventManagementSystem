package com.taru.eventmanagement.controllers;

import com.taru.eventmanagement.config.SecurityUtil;
import com.taru.eventmanagement.dto.EventDTO;
import com.taru.eventmanagement.dto.UserDTO;
import com.taru.eventmanagement.exception.AccessDeniedException;
import com.taru.eventmanagement.exception.MyNotFoundException;
import com.taru.eventmanagement.models.EventAttendeeId;
import com.taru.eventmanagement.models.UserRole;
import com.taru.eventmanagement.repositories.EventAttendeeRepository;
import com.taru.eventmanagement.repositories.UserRoleRepository;
import com.taru.eventmanagement.services.EventAttendeeService;
import com.taru.eventmanagement.services.EventService;
import com.taru.eventmanagement.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EventController {

    private final EventService eventService;
    private final UserService userService;
    private final EventAttendeeService eventAttendeeService;
    private final EventAttendeeRepository eventAttendeeRepository;
    private final UserRoleRepository userRoleRepository;

    public EventController(EventService eventService, UserService userService, EventAttendeeService eventAttendeeService, EventAttendeeRepository eventAttendeeRepository, UserRoleRepository userRoleRepository) {
        this.eventService = eventService;
        this.userService = userService;
        this.eventAttendeeService = eventAttendeeService;
        this.eventAttendeeRepository = eventAttendeeRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @GetMapping("/event")
    public String listEvents(Model model) {

        List<EventDTO> events = eventService.getAllEvents();

        model.addAttribute("events", events);

        return "event-list";
    }

    @GetMapping("/user/{userId}/created-events")
    public String eventListByCreatorId(@PathVariable("userId") int userId, Model model) {

        List<EventDTO> events = eventService.getAllEventsByCreatorId(userId);

        model.addAttribute("events", events);

        return "event-list";
    }

    @GetMapping("/user/{userId}/attended-events")
    public String eventListByAttendeeId(@PathVariable("userId") int userId, Model model) {

        List<EventDTO> events = eventAttendeeService.getAllEventsByAttendeeId(userId);

        model.addAttribute("events", events);

        return "event-list";
    }

    @GetMapping("/event/search")
    public String searchClub(@RequestParam(value = "query") String query, Model model) {

        List<EventDTO> events = eventService.searchEvents(query);

        model.addAttribute("events", events);

        return "event-list";
    }

    @GetMapping("/event/create")
    public String createEventForm(Model model) {

        model.addAttribute("event", new EventDTO());
        return "event-create";
    }

    @PostMapping("/event/create")
    public String createEvent(@Valid @ModelAttribute("event") EventDTO event, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("event", event);
            return "event-create";
        }

        int eventId = eventService.createEvent(event).getEventId();

        return "redirect:/event/%d?success".formatted(eventId);
    }

    @GetMapping("/event/{eventId}/edit")
    public String updateEventForm(@PathVariable("eventId") int eventId, Model model) {

        EventDTO event = eventService.getEventById(eventId);

        String sessionUsername = SecurityUtil.getSessionUser();
        UserDTO sessionUser = userService.getUserByUsername(sessionUsername);
        UserRole sessionUserRole = userRoleRepository.findByUserUserId(sessionUser.getUserId())
                .orElseThrow(() -> new MyNotFoundException("User with username = " + sessionUser.getUserId() + " - not found!"));

        if (sessionUser.getUserId() != event.getCreator().getUserId() && !sessionUserRole.getRole().getName().equals("ROLE_ADMIN")){
            throw new AccessDeniedException("Access denied!\nYou do not have rights to edit Events that you did not create.");
        }

        model.addAttribute("event", event);
        return "event-edit";
    }

    @PostMapping("/event/{eventId}/edit")
    public String editEvent(@PathVariable("eventId") int eventId, @Valid @ModelAttribute("event") EventDTO event, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("event", event);
            return "event-edit";
        }

        event.setEventId(eventId);
        eventService.updateEventById(eventId, event);

        return "redirect:/event/%d?success".formatted(eventId);
    }

    @GetMapping("/event/{eventId}")
    public String eventDetails(@PathVariable("eventId") int eventId, Model model) {

        EventDTO event = eventService.getEventById(eventId);

        model.addAttribute("event", event);

        String username = SecurityUtil.getSessionUser();
        UserDTO user = new UserDTO();
        if (username != null) {
            user = userService.getUserByUsername(username);
        }

        model.addAttribute("isAttended", eventAttendeeRepository.existsById(
                new EventAttendeeId(eventId, user.getUserId()))
        );
        model.addAttribute("user", user);

        return "event-details";
    }

    @GetMapping("/event/{eventId}/delete")
    public String deleteEvent(@PathVariable("eventId") int eventId) {

        eventService.deleteEventById(eventId);

        return "redirect:/event?success";
    }
}
