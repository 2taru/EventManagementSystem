package com.taru.eventmanagement.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GlobalController {

    @GetMapping("/about")
    public String about(){

        return "about";
    }

}
