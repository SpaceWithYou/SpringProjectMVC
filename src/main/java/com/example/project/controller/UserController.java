package com.example.project.controller;

import com.example.project.entity.UserAnswer;
import com.example.project.entity.UserTask;
import com.example.project.service.TaskService;
import com.example.project.util.UserDetails;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@Secured("hasRole('USER')")
public class UserController {

    @Autowired
    private TaskService service;

    @Autowired
    EntityManager manager;

    @GetMapping("/user/{id}")
    public Optional<UserTask> getUserTask(@PathVariable UUID id) {
        return service.getTaskById(id);
    }

    @GetMapping("/user/")
    public List<UserTask> getAllUserTask(Authentication auth) {
        //get current id
        UserDetails details = (UserDetails) auth.getDetails();
        return service.getAllUserTasks(details.getUser().getId());
    }

    /**Send answers to problem num of task with id*/
    @PostMapping("/user/{id}/{num}")
    @Transactional
    public String sendAnswers(@PathVariable UUID taskId, @PathVariable long num,
                              @ModelAttribute List<String> answers) {
        //Current Date
        UserAnswer answer = new UserAnswer(answers, num, taskId, new Date());
        //Save in db
        manager.persist(answer);
        return "Answers was sent";
    }
}
