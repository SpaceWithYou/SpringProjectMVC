package com.example.project.controller;

import org.springframework.core.annotation.Order;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

//Home page controller
@Controller
public class HomeController {
    @GetMapping("/")
    @Order(value = Integer.MIN_VALUE)
    @PreAuthorize("permitAll()")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/auth")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ModelAndView userPage() {
        return new ModelAndView("userPage");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('SUPER_USER')")
    public ModelAndView superUserPage() {
        return new ModelAndView("superUserPage");
    }
}
