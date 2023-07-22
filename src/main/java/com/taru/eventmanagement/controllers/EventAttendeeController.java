package com.taru.eventmanagement.controllers;

import com.taru.eventmanagement.config.SecurityUtil;
import com.taru.eventmanagement.dto.EventAttendeeDTO;
import com.taru.eventmanagement.dto.UserDTO;
import com.taru.eventmanagement.models.EventAttendeeId;
import com.taru.eventmanagement.services.EventAttendeeService;
import com.taru.eventmanagement.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class EventAttendeeController {

    private final EventAttendeeService eventAttendeeService;
    private final UserService userService;

    public EventAttendeeController(EventAttendeeService eventAttendeeService, UserService userService) {
        this.eventAttendeeService = eventAttendeeService;
        this.userService = userService;
    }

    @GetMapping("/event/{eventId}/attend")
    public String attendToEvent(
            @PathVariable("eventId") int eventId,
            Model model)
    {

        eventAttendeeService.createEventAttendee(eventId, EventAttendeeDTO.builder().status("Attended").build());

        model.addAttribute("isAttended", true);

        return "redirect:/event/%d?success".formatted(eventId);
    }

    @GetMapping("/event/{eventId}/skip")
    public String skipToEvent(
            @PathVariable("eventId") int eventId,
            Model model)
    {

        String username = SecurityUtil.getSessionUser();
        UserDTO userDTO = userService.getUserByUsername(username);

        eventAttendeeService.deleteEventAttendeeById(new EventAttendeeId(eventId, userDTO.getUserId()));

        model.addAttribute("isAttended", false);

        return "redirect:/event/%d?success".formatted(eventId);
    }
}
