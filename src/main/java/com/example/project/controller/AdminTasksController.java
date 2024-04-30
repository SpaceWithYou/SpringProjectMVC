package com.example.project.controller;

import com.example.project.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;

@Controller
@Secured("hasRole('SUPER_USER')")
public class AdminTasksController {
    @Autowired
    TaskService service;
}
