package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.entity.UserAnswer;
import com.example.project.repository.UserRepository;
import com.example.project.util.TaskChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

//ToDo container, notify users with message broker, add cache
/**Check answers and notify users*/
@Service
public class TaskCheckerService {
    @Autowired
    private TaskChecker service;

    @Autowired
    private UserRepository userRepo;

    // >= 1
    private static final int threadsNum =
            Runtime.getRuntime().availableProcessors() != 1 ?
                    7 * Runtime.getRuntime().availableProcessors() / 10 : 1;

    /**Process all marks*/
    public List<Future<Float>> checkUserTasks(long id, List<UserAnswer> answers) {
        Optional<User> opt = userRepo.findById(id);
        if(opt.isEmpty() || answers.isEmpty()) return null;
        //process in thread pool with work stealing
        try(ForkJoinPool pool = new ForkJoinPool(threadsNum)) {
            //task list for thread pool
            List<Callable<Float>> taskList = new ArrayList<>(answers.size());
            //fill list
            answers.forEach(x -> taskList.add(() ->
                    service.checkProblem(x.getAnswers(), x.getTaskId(), x.getProblemNum())
            ));
            return pool.invokeAll(taskList);
        }
    }
}
