package com.example.project.controller;

import com.example.project.entity.UserAnswer;
import com.example.project.entity.UserTask;
import com.example.project.service.TaskCheckerService;
import com.example.project.service.TaskService;
import com.example.project.util.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

//ToDo add cache
@RestController
@Secured("hasRole('USER')")
public class UserController {

    @Autowired
    private TaskService service;

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
        UserAnswer answer = new UserAnswer(answers, num, details.getUser().getId(), taskId, LocalDateTime.now());
        //Save in db
        service.saveAnswer(answer);
        return "Answers was sent";
    }

    /**Send all answers to task*/
    @PostMapping("/user/tasks/{taskId}")
    public String sendAnswers(@PathVariable UUID taskId, @RequestBody List<List<String>> answers,
                              Authentication auth) {
        //get current id
        UserDetails details = (UserDetails) auth.getPrincipal();
        int counter = 0;
        LocalDateTime date = LocalDateTime.now();
        for (List<String> problemAnswer: answers) {
            UserAnswer answer = new UserAnswer(problemAnswer, counter, details.getUser().getId(), taskId, date);
            //Save in db
            service.saveAnswer(answer);
            counter++;
        }
        return "All answers was sent";
    }

    /**Get history of answers*/
    @GetMapping("/user/history")
    public String getAnswers(Authentication auth) {
        UserDetails details = (UserDetails) auth.getPrincipal();
        Iterable<UserAnswer> answerList = service.getAllUserAnswers(details.getUser().getId());
        if(answerList == null) return "null";
        StringBuilder builder = new StringBuilder();
        //TODO add task id
        for (UserAnswer answer : answerList) {
            builder.append('[');
            builder.append(answer.getAnswers()
                    .stream().
                    collect(Collectors.
                            joining(", ")));
            builder.append("]\n");
        }
        return builder.toString();
    }

    /**Async process user marks*/
    @GetMapping("/user/marks")
    public ResponseBodyEmitter getMarks(Authentication auth) throws IOException {
        UserDetails details = (UserDetails) auth.getPrincipal();
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        long userId = details.getUser().getId();
        Iterable<UserAnswer> answers = service.getAllUserAnswers(userId);
        //create answer list
        List<UserAnswer> answerList =  new ArrayList<>();
        answers.forEach(answerList::add);
        try {
            checkerService.checkUserTasks(answerList);
            List<Float> marks = checkerService.getTasksResult(emitter);
            //return completed tasks in stream
            if(marks.isEmpty()) {
                emitter.send("No data");
            }
            emitter.complete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return emitter;
    }
}
