package com.taru.eventmanagement.controllers;

import com.taru.eventmanagement.dto.UserDTO;
import com.taru.eventmanagement.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginForm(){

        return "login";
    }

    @GetMapping("/register")
    public String getRegisterForm(Model model){

        model.addAttribute("user", new UserDTO());

        return "register";
    }

    @PostMapping("/register/save")
    public String register(@ModelAttribute("user")UserDTO user, Model model) {
//        UserDTO existingUserEmail = userService.getUserByEmail(user.getEmail());
//        if(existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()) {
//            return "redirect:/register?fail";
//        }
//        UserDTO existingUserUsername = userService.getUserByUsername(user.getUsername());
//        if(existingUserUsername != null && existingUserUsername.getUsername() != null && !existingUserUsername.getUsername().isEmpty()) {
//            return "redirect:/register?fail";
//        }
//        if(result.hasErrors()) {
//            model.addAttribute("user", user);
//            return "register";
//        }
        userService.createUser(user);
        return "redirect:/event";
    }
}
