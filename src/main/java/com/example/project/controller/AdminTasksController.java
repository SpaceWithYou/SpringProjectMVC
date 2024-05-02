package com.example.project.controller;

import com.example.project.entity.UserTask;
import com.example.project.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@Secured("hasRole('SUPER_USER')")
public class AdminTasksController {
    @Autowired
    TaskService service;

    private static final String path = "/admin/tasks/";

    @GetMapping(path)
    public Iterable<UserTask> getAllTasks() {
        return service.getAllTasks();
    }

    @GetMapping(path + "{id}")
    public List<UserTask> getAllUserTask(@PathVariable long id) {
        return service.getAllUserTasks(id);
    }

    @GetMapping(path + "{userId}")
    public Optional<UserTask> getUserTask(@PathVariable UUID userId) {
        return service.getTaskById(userId);
    }

    @PostMapping(path)
    public String createTask(@ModelAttribute UserTask task) {
        service.addUserTask(task, task.getUserId());
        return "Task created";
    }

    @PutMapping(path + "{id}")
    public String updateTask(@PathVariable UUID id, @ModelAttribute UserTask task) {
        service.updateUserTask(task, id);
        return "Task updated";
    }

    @DeleteMapping(path + "{id}")
    public void deleteTask(@PathVariable UUID id) {
        service.deleteUserTask(id);
    }
}
