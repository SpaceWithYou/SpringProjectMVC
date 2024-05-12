package com.example.project.service;

import com.example.project.entity.UserAnswer;
import com.example.project.util.TaskChecker;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

//ToDo container, notify users with message broker, add cache
/**Check answers and notify users*/
@Getter
@Service
public class TaskCheckerService {

    @Autowired
    private TaskChecker service;

    // >= 1
    private static final int threadsNum =
            Runtime.getRuntime().availableProcessors() != 1 ?
                    6 * Runtime.getRuntime().availableProcessors() / 10 : 1;

    private List<CompletableFuture<Float>> tasks = new ArrayList<>();

    /**Process all marks
     * fill up task list*/
    public void checkUserTasks(List<UserAnswer> answers) throws InterruptedException {
        //process in thread pool
        try(ExecutorService pool = Executors.newFixedThreadPool(threadsNum)) {
            tasks = service.checkAllProblemsParallel(answers, pool);
        }
    }

    /**Returns result of tasks*/
    public List<Float> getTasksResult(ResponseBodyEmitter emitter) throws ExecutionException, InterruptedException, IOException {
        List<Float> result = new ArrayList<>();
        for (Future<Float> task : tasks) {
            result.add(task.get());
            emitter.send(task.get() + "\n");
        }
        return result;
    }
}
