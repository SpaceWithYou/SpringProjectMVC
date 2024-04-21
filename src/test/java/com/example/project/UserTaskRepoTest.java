package com.example.project;

import com.example.project.entity.UserTask;
import com.example.project.repository.UserTaskRepository;
import com.example.project.util.TaskProblem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.properties"})
public class UserTaskRepoTest {
    @Autowired
    UserTaskRepository repo;
    static List<TaskProblem> problemList;

    @BeforeAll
    static void fillProblemList() {
        problemList = new ArrayList<>();

        HashMap<String, Boolean> ansMap = new HashMap<>();
        ansMap.put("5", false);
        ansMap.put("3", false);
        ansMap.put("4", true);
        TaskProblem problem = new TaskProblem("2+2=?", ansMap);
        problemList.add(problem);

        TreeMap<String, Boolean> ansMap1 = new TreeMap<>();
        ansMap1.put("Moscow", true);
        ansMap1.put("Saint Petersburg", false);
        ansMap1.put("Novosibirsk", false);
        TaskProblem problem2 = new TaskProblem("Capital of Russia is?", ansMap1);
        problemList.add(problem2);

        LinkedHashMap<String, Boolean> ansMap2 = new LinkedHashMap<>();
        ansMap2.put("Java Persistence API", true);
        ansMap2.put("Jakarta Persistence API", true);
        ansMap2.put("Java POJO API", false);
        ansMap2.put("Java Path Application", false);
        TaskProblem problem3 = new TaskProblem("JPA is?", ansMap2);
        problemList.add(problem3);
    }

    @Test
    void saveTaskTest() {
        repo.deleteAll();
        UserTask task = new UserTask("Name", 1L);
        task.addProblemTask(problemList.get(0));
        repo.save(task);
        UUID id = task.getId();
        assert repo.existsById(id);
    }

    @Test
    void getTaskTest() {
        repo.deleteAll();
        UserTask task = new UserTask("Name",1L, problemList);
        repo.save(task);
        UUID id = task.getId();
        Optional<UserTask> userTask = repo.findById(id);
        assert userTask.isPresent();
        UserTask task2 = userTask.get();
        assert task2.getTaskName().equals("Name");
        assert task2.getUserId() == 1L;
        assert task2.getId() == id;
        List<TaskProblem> taskProblemList = task2.getProblem();
        int i = 0;
        for (TaskProblem ptask: taskProblemList) {
            assert ptask.equals(taskProblemList.get(i));
            i++;
        }
    }

    @Test
    void updateTaskTest() {
        repo.deleteAll();
        UserTask task = new UserTask("Name",1L);
        task.addProblemTask(problemList.get(0));
        repo.save(task);

        UserTask uTask = new UserTask("Name2",2L);
        uTask.addProblemTask(problemList.get(1));
        uTask.setId(task.getId());
        repo.save(uTask);

        Optional<UserTask> saved = repo.findById(uTask.getId());
        assert saved.isPresent();
        UserTask savedTask = saved.get();
        assert savedTask.getTaskName().equals("Name2");
        assert savedTask.getUserId() == 2L;
        TaskProblem problem = savedTask.getProblem().get(0);

        assert problem.getName().equals("Capital of Russia is?");
        assert problem.getAnswerMap().equals(uTask.getProblem().get(0).getAnswerMap());
    }

    @Test
    void deleteTaskTest() {
        repo.deleteAll();
        UserTask task = new UserTask("Name",1L, problemList);
        repo.save(task);
        repo.deleteById(task.getId());
        assert !repo.findAll().iterator().hasNext();

        repo.save(task);
        repo.delete(task);
        assert !repo.findAll().iterator().hasNext();
    }
}
