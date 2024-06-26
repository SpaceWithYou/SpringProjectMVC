package com.example.project.util;

import com.example.project.entity.UserAnswer;
import com.example.project.entity.UserTask;
import com.example.project.repository.UserTaskRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**checker service answers*/
@Component
@Getter
@Setter
public class TaskChecker {

    @Autowired
    private volatile UserTaskRepository taskRepo;

    private boolean ignoreLowerUpperCase = true;

    /**Helper function, iterate through answers and checks right answer in map*/
    private float workerProblem(List<String> answers, Map<String, Boolean> map) {
        int userAns;
        //count all correct answers
        int rightAns = (int) map.keySet().stream().filter(map::get).count();
        if(ignoreLowerUpperCase) {
            //collect all correct answers and create new map with lowercase answers
            HashMap<String, Boolean> lowerCaseMap = new HashMap<>(rightAns);
            map.keySet().stream().filter(map::get)
                    .forEach(x -> lowerCaseMap.put(x, true));
            //check answers
            userAns = (int) answers.stream().filter(x -> lowerCaseMap.containsKey(x.toLowerCase()))
                    .count();

        } else {
            userAns = (int) map.keySet().stream().filter(map::containsKey)
                    .count();
        }
        return rightAns == 0 ? -1 : (float) userAns / rightAns;
    }
    //TODO workerUserTask
    /**Checks answers for problemNum problem,
     * @return number of selected right answers / number of right answers*/
    public float checkProblem(List<String> answers, TaskProblem problem) {
        return workerProblem(answers, problem.getAnswerMap());
    }

    /**Checks answers for problemNum problem,
     * @return number of selected right answers / number of right answers*/
    public float checkProblem(List<String> answers, UUID taskId, int problemNum) {
        Optional<UserTask> opt = taskRepo.findById(taskId);
        if(opt.isEmpty()) return -1;
        UserTask task = opt.get();
        if(task.getProblem().size() < problemNum || problemNum < 0) return -1;
        Map<String, Boolean> map = task.getProblem().get(problemNum).getAnswerMap();
        return workerProblem(answers, map);
    }

    /**Check answers
     * @return list of fractions: number of selected right answers / number of right answers <br>
     * number of answers.size() must be equal number of problems in task*/
    public List<Float> checkUserTask(List<List<String>> answers, UUID taskId) {
        Optional<UserTask> opt = taskRepo.findById(taskId);
        if(opt.isEmpty() || opt.get().getProblem().size() != answers.size()) return null;
        UserTask task = opt.get();
        ArrayList<Float> res = new ArrayList<>(answers.size());
        for (int i = 0; i < answers.size(); i++) {
            res.add(workerProblem(answers.get(i), task.getProblem().get(i).getAnswerMap()));
        }
        return res;
    }

    /**Check answers
     * @return list of fractions: number of selected right answers / number of right answers <br>
     * number of answers.size() must be equal number of problems in task*/
    public List<Float> checkUserTask(List<List<String>> answers, UserTask task) {
        if(task.getProblem().size() != answers.size()) return null;
        ArrayList<Float> res = new ArrayList<>(answers.size());
        for (int i = 0; i < answers.size(); i++) {
            res.add(workerProblem(answers.get(i), task.getProblem().get(i).getAnswerMap()));
        }
        return res;
    }

    /**Check answers parallel via ExecutorService
     * @return list of fractions: number of selected right answers / number of right answers <br>
     * number of answers.size() must be equal number of problems in task**/
    public List<CompletableFuture<Float>> checkAllProblemsParallel(List<UserAnswer> answers, ExecutorService service) {
        List<CompletableFuture<Float>> futures = new ArrayList<>();
        for (var ans: answers) {
            var optTask = taskRepo.findById(ans.getTaskId());
            if(optTask.isEmpty()) continue;
            for (TaskProblem problem : optTask.get().getProblem()) {
                CompletableFuture.supplyAsync(() -> workerProblem(ans.getAnswers(), problem.getAnswerMap()));
            }
        }
        return futures;
    }
}
