package com.example.project.controller;

import com.example.project.entity.UserTask;
import com.example.project.service.TaskService;
import com.example.project.util.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@Secured("hasRole('USER')")
public class UserController {

    @Autowired
    private TaskService service;

    @GetMapping("/user/{id}")
    public Optional<UserTask> getUserTask(@PathVariable UUID id) {
        return service.getTaskById(id);
    }

    @GetMapping("/user/")
    public List<UserTask> getAllUserTask(Authentication auth) {
        UserDetails details = (UserDetails) auth.getDetails();
        return service.getAllUserTasks(details.getUser().getId());
    }

    //TODO post check answer
}
