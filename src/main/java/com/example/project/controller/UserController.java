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
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Secured("hasRole('USER')")
public class UserController {

    @Autowired
    private TaskService service;

    @Autowired
    private EntityManager manager;

    @GetMapping("/user/tasks/{id}")
    public Optional<UserTask> getUserTask(@PathVariable UUID id) {
        return service.getTaskById(id);
    }

    @GetMapping("/user/tasks/")
    public List<UserTask> getAllUserTask(Authentication auth) {
        //get current id
        UserDetails details = (UserDetails) auth.getPrincipal();
        return service.getAllUserTasks(details.getUser().getId());
    }

    /**Send answers to problem num of task with id*/
    @PostMapping("/user/tasks/{id}/{num}")
    @Transactional
    public String sendAnswers(@PathVariable UUID taskId, @PathVariable long num,
                              @RequestBody List<String> answers) {
        //Current Date
        UserAnswer answer = new UserAnswer(answers, num, taskId, new Date());
        //Save in db
        manager.persist(answer);
        return "Answers was sent";
    }
}
