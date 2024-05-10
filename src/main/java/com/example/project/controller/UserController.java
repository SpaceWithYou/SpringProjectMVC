package com.example.project.controller;

import com.example.project.entity.UserAnswer;
import com.example.project.entity.UserTask;
import com.example.project.repository.UserAnswerRepository;
import com.example.project.service.TaskCheckerService;
import com.example.project.service.TaskService;
import com.example.project.util.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

//ToDo add cache
@RestController
@Secured("hasRole('USER')")
public class UserController {

    @Autowired
    private TaskService service;

    @Autowired
    private UserAnswerRepository answerRepo;

    @Autowired
    private TaskCheckerService checkerService;
    //ToDo answer cache

    @GetMapping("/user/tasks/{id}")
    public Optional<UserTask> getUserTask(@PathVariable UUID id) {
        return service.getTaskById(id);
    }

    @GetMapping("/user/tasks")
    public List<UserTask> getAllUserTask(Authentication auth) {
        //get current id
        UserDetails details = (UserDetails) auth.getPrincipal();
        return service.getAllUserTasks(details.getUser().getId());
    }

    /**Send answers to problem num of task with id*/
    @PostMapping("/user/tasks/{taskId}/{num}")
    public String sendAnswers(@PathVariable UUID taskId, @PathVariable int num,
                              @RequestBody List<String> answers, Authentication auth) {
        //get current id
        UserDetails details = (UserDetails) auth.getPrincipal();
        //Current Date
        UserAnswer answer = new UserAnswer(answers, num, details.getUser().getId(), taskId, new Date());
        //Save in db
        answerRepo.save(answer);
        return "Answers was sent";
    }

    /**Send all answers to task*/
    @PostMapping("/user/tasks/{taskId}")
    public String sendAnswers(@PathVariable UUID taskId, @RequestBody List<List<String>> answers,
                              Authentication auth) {
        //get current id
        UserDetails details = (UserDetails) auth.getPrincipal();
        int counter = 0;
        for (List<String> problemAnswer: answers) {
            UserAnswer answer = new UserAnswer(problemAnswer, counter, details.getUser().getId(), taskId, new Date());
            //Save in db
            answerRepo.save(answer);
            counter++;
        }
        return "All answers was sent";
    }

    /**Get history of answers*/
    @GetMapping("/user/history")
    public String getAnswers(Authentication auth) {
        UserDetails details = (UserDetails) auth.getPrincipal();
        List<UserAnswer> answerList = service.getAllUserAnswers(details.getUser().getId());
        if(answerList == null) return "null";
        StringBuilder builder = new StringBuilder();
        for (UserAnswer answer : answerList) {
            builder.append('[');
            for(String ans : answer.getAnswers()) {
                builder.append(ans);
                builder.append(", ");
            }
            builder.append("]\n");
        }
        return builder.toString();
    }

    /**Async process user marks*/
    //@Async
    @GetMapping("/user/marks")
    public CompletableFuture<String> getMarks(Authentication auth) {
        UserDetails details = (UserDetails) auth.getPrincipal();
        long userId = details.getUser().getId();
        List<Future<Float>> futures = checkerService.checkUserTasks(userId, service.getAllUserAnswers(userId));
        if(futures.isEmpty()) return CompletableFuture.supplyAsync(() -> "No data");
        CompletableFuture<String> res = CompletableFuture.supplyAsync(() -> {
            StringBuilder b = new StringBuilder();
            for (var task: futures) {
                if(task.isDone()) {
                    try {
                        b.append(task.get());
                        b.append('\n');
                    } catch (Exception e) {
                        continue;
                    }
                }
            }
            return b.toString();
        });
        return res;
    }
}
