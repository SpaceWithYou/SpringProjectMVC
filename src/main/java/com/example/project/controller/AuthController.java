package com.example.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @GetMapping("/user")
    public ModelAndView userPage() {
        return new ModelAndView("userPage");
    }

    @GetMapping("/admin")
    public ModelAndView superUserPage() {
        return new ModelAndView("superUserPage");
    }
}
