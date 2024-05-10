package com.example.project.service;

import com.example.project.entity.User;
import com.example.project.entity.UserAnswer;
import com.example.project.entity.UserTask;
import com.example.project.repository.UserAnswerRepository;
import com.example.project.repository.UserRepository;
import com.example.project.repository.UserTaskRepository;
import com.example.project.util.TaskProblem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**Service for managing tasks and answers*/
@Service
public class TaskService {

    @Autowired
    private UserTaskRepository taskRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserAnswerRepository answerRepo;

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

    public List<UserAnswer> getAllUserAnswers(long userId) {
        return userRepo.existsById(userId) ? answerRepo.getAnswersByUserId(userId) :
                null;
    }

}
