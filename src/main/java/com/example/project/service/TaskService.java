package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.entity.UserTask;
import com.example.project.repository.UserRepository;
import com.example.project.repository.UserTaskRepository;
import com.example.project.util.TaskProblem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**Service for managing tasks, notification users, etc...*/
@Service
public class TaskService {

    @Autowired
    private UserTaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    @Getter @Setter
    private boolean ignoreLowerUpperCase = true;

    public Iterable<UserTask> getAllTasks() {
        return taskRepo.findAll();
    }

    public Optional<UserTask> getTaskById(UUID id) {
        return taskRepo.findById(id);
    }

    public List<UserTask> getAllUserTasks(String name) {
        Optional<User> opt = userRepo.findByName(name);
        return opt.map(User::getTaskList).orElse(null);
    }

    public List<UserTask> getAllUserTasks(long id) {
        Optional<User> opt = userRepo.findById(id);
        return opt.map(User::getTaskList).orElse(null);
    }

    public void addUserTask(UserTask task, long userId) {
        task.setUserId(userId);
        taskRepo.save(task);
    }

    public void deleteUserTask(UUID id) {
        taskRepo.deleteById(id);
    }

    public void updateUserTask(UserTask task, UUID id) {
        Optional<UserTask> opt = taskRepo.findById(id);
        if(opt.isEmpty()) return;
        UserTask optTask = opt.get();
        task.setId(optTask.getId());
        taskRepo.save(task);
    }

    public void updateUserTaskProblems(List<TaskProblem> newProblems, UUID taskId) {
        Optional<UserTask> opt = taskRepo.findById(taskId);
        if(opt.isEmpty()) {
            return;
        }
        opt.get().getProblem().clear();
        opt.get().getProblem().addAll(newProblems);
    }

    /**Helper function*/
    private float worker(List<String> answers, Map<String, Boolean> map) {
        int rightAns = 0, userAns = 0;
        /* count all correct answers
         * if ignoreCase then replace by lowercase*/
        for (String mapAns: map.keySet()) {
            if(map.get(mapAns)) {
                rightAns++;
            }
            if(ignoreLowerUpperCase) {
                map.put(mapAns.toLowerCase(), map.get(mapAns));
                map.remove(mapAns);
            }
        }
        for(String answer: answers) {
            if(ignoreLowerUpperCase) {
                if(map.containsKey(answer.toLowerCase()) && map.get(answer.toLowerCase())) {
                    userAns++;
                }
            } else {
                if(map.containsKey(answer) && map.get(answer)) {
                    userAns++;
                }
            }
        }
        return (float) userAns / rightAns;
    }

    /**Checks answers for problemNum problem,
     * @return number of selected right answers / number of right answers*/
    public float checkProblem(List<String> answers, UUID taskId, int problemNum) {
        Optional<UserTask> opt = taskRepo.findById(taskId);
        if(opt.isEmpty()) return -1;
        UserTask task = opt.get();
        if(task.getProblem().size() < problemNum || problemNum < 0) return -1;
        Map<String, Boolean> map = task.getProblem().get(problemNum).getAnswerMap();
        return worker(answers, map);
    }

    /**Checks answers for problemNum problem,
     * @return number of selected right answers / number of right answers*/
    public float checkProblem(List<String> answers, TaskProblem problem) {
        return worker(answers, problem.getAnswerMap());
    }

    /**Check answers
     * @return list of fractions: number of selected right answers / number of right answers <br>
     * number of answers.size() must be equal number of problems in task*/
    public List<Float> checkAllProblems(List<List<String>> answers, UUID taskId) {
        Optional<UserTask> opt = taskRepo.findById(taskId);
        if(opt.isEmpty() || opt.get().getProblem().size() != answers.size()) return null;
        UserTask task = opt.get();
        ArrayList<Float> res = new ArrayList<>(answers.size());
        for (int i = 0; i < answers.size(); i++) {
            res.add(worker(answers.get(i), task.getProblem().get(i).getAnswerMap()));
        }
        return res;
    }

    /**Check answers
     * @return list of fractions: number of selected right answers / number of right answers <br>
     * number of answers.size() must be equal number of problems in task*/
    public List<Float> checkAllProblems(List<List<String>> answers, UserTask task) {
        if(task.getProblem().size() != answers.size()) return null;
        ArrayList<Float> res = new ArrayList<>(answers.size());
        for (int i = 0; i < answers.size(); i++) {
            res.add(worker(answers.get(i), task.getProblem().get(i).getAnswerMap()));
        }
        return res;
    }

}
