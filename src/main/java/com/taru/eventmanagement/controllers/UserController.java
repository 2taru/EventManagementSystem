package com.taru.eventmanagement.controllers;

import com.taru.eventmanagement.config.SecurityUtil;
import com.taru.eventmanagement.dto.UserDTO;
import com.taru.eventmanagement.repositories.EventAttendeeRepository;
import com.taru.eventmanagement.repositories.EventRepository;
import com.taru.eventmanagement.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UserController {

    private final UserService userService;
    private final EventRepository eventRepository;
    private final EventAttendeeRepository eventAttendeeRepository;

    public UserController(UserService userService, EventRepository eventRepository, EventAttendeeRepository eventAttendeeRepository) {
        this.userService = userService;
        this.eventRepository = eventRepository;
        this.eventAttendeeRepository = eventAttendeeRepository;
    }

    @GetMapping("/user/{username}")
    public String userPage(@PathVariable("username") String username, Model model) {

        UserDTO user = new UserDTO();
        if (username != null) {
            user = userService.getUserByUsername(username);
        }

        boolean allowEditProfile = user.getUsername().equals(SecurityUtil.getSessionUser());
        int createdEventsCount = eventRepository.countByCreatorUserId(user.getUserId());
        int attendedEventsCount = eventAttendeeRepository.countByAttendeeUserId(user.getUserId());

        model.addAttribute("allowEditProfile", allowEditProfile);
        model.addAttribute("createdEventsCount", createdEventsCount);
        model.addAttribute("attendedEventsCount", attendedEventsCount);
        model.addAttribute("user", user);

        return "user-details";
    }
}
