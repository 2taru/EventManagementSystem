package com.taru.eventmanagement.controllers;

import com.taru.eventmanagement.dto.UserDTO;
import com.taru.eventmanagement.models.User;
import com.taru.eventmanagement.repositories.UserRepository;
import com.taru.eventmanagement.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final UserService userService;

    public AuthController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginForm() {

        return "user/login";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model) {

        model.addAttribute("user", new UserDTO());

        return "user/register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") UserDTO user, BindingResult bindingResult, Model model) {

        User existingUserEmail = userRepository.findByEmail(user.getEmail()).orElse(null);
        if (existingUserEmail != null && existingUserEmail.getEmail().equals(user.getEmail())) {
            return "redirect:/register?fail";
        }

        User existingUserUsername = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (existingUserUsername != null && existingUserUsername.getUsername().equals(user.getUsername())) {
            return "redirect:/register?fail";
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "user/register";
        }

        userService.createUser(user);
        return "redirect:/login?success";
    }
}
