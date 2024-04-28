package com.example.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

//Home page controller
@Controller
public class HomeController {
    @GetMapping(path = {"/index", "/"})
    public ModelAndView startPage() {
        return new ModelAndView("index");
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }
}
