package com.example.project;

import com.example.project.entity.User;
import com.example.project.entity.UserTask;
import com.example.project.repository.UserRepository;
import com.example.project.repository.UserTaskRepository;
import com.example.project.util.TaskProblem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.*;

@DataJpaTest(properties = {"spring.config.location=classpath:application-test.properties"})
public class UserRepoTest {
    @Autowired
    UserRepository repo;

    @Autowired
    UserTaskRepository taskRepo;

    private static List<UserTask> list;

    @BeforeAll
    static void fillProblemList() {
        //Math task
        list = new ArrayList<>();
        UserTask mTask = new UserTask("Math task", 1L);
        TaskProblem problem = new TaskProblem("2+2=?", new HashMap<>());
        problem.addAnswer("5", false);
        problem.addAnswer("3", false);
        problem.addAnswer("2", false);
        problem.addAnswer("4", true);
        mTask.addProblemTask(problem);

        problem = new TaskProblem("5-3=?", new TreeMap<>());
        problem.addAnswer("0", false);
        problem.addAnswer("1", false);
        problem.addAnswer("2", true);
        problem.addAnswer("3", false);
        mTask.addProblemTask(problem);

        problem = new TaskProblem("7*8=?", new LinkedHashMap<>());
        problem.addAnswer("56", true);
        problem.addAnswer("43", false);
        problem.addAnswer("20", true);
        problem.addAnswer("-1", false);
        mTask.addProblemTask(problem);

        list.add(mTask);
        //
        UserTask gTask = new UserTask("Geography test", 2L);
        problem = new TaskProblem("Capital of Russia is?", new HashMap<>());
        problem.addAnswer("Moscow", true);
        problem.addAnswer("Saint Petersburg", false);
        problem.addAnswer("Novosibirsk", false);
        gTask.addProblemTask(problem);

        problem = new TaskProblem("London is the capital of?", new TreeMap<>());
        problem.addAnswer("Great Britain", true);
        problem.addAnswer("USA", false);
        problem.addAnswer("Brazil", false);
        problem.addAnswer("France", false);
        gTask.addProblemTask(problem);

        problem = new TaskProblem("What is the largest continent?", new LinkedHashMap<>());
        problem.addAnswer("North America", false);
        problem.addAnswer("Eurasia", true);
        problem.addAnswer("Africa", false);
        problem.addAnswer("South America", false);
        problem.addAnswer("Australia", false);
        gTask.addProblemTask(problem);

        list.add(gTask);
        //
        UserTask javaTask = new UserTask("Java task", 1L);
        problem = new TaskProblem("Class object contains class:", new HashMap<>());
        problem.addAnswer("hashCode", true);
        problem.addAnswer("equals", true);
        problem.addAnswer("isEmpty", false);
        problem.addAnswer("isNull", false);
        javaTask.addProblemTask(problem);

        problem = new TaskProblem("JPA is?", new TreeMap<>());
        problem.addAnswer("Java Persistence API", true);
        problem.addAnswer("Jakarta Persistence API", true);
        problem.addAnswer("Java POJO API", false);
        problem.addAnswer("Java Path Application", false);
        javaTask.addProblemTask(problem);

        problem = new TaskProblem("JDK is?", new LinkedHashMap<>());
        problem.addAnswer("Java Development Kit", true);
        problem.addAnswer("Java Dynamic Kit", false);
        problem.addAnswer("Java DDL Kit", false);
        javaTask.addProblemTask(problem);

        list.add(javaTask);
    }

    @Test
    void repoSaveTest() {
        repo.deleteAll();
        User user = new User("Name", "pass", false, list);
        repo.save(user);
        assert repo.existsById(1L);
    }

    @Test
    void repoGetTest() {
        repo.deleteAll();
        User user = new User("Name", "pass", true);
        user = repo.save(user);
        user.addTask(list.get(0));
        user.addTask(list.get(2));
        taskRepo.saveAll(user.getTaskList());

        Optional<User> optU = repo.findById(user.getId());
        assert optU.isPresent();
        User user2 = optU.get();
        assert user2.getName().equals("Name");
        assert user2.getPassword().equals("pass");
        assert user2.isSuperUser();
        assert user2.getTaskList().get(0).getTaskName().equals("Math task");
        assert user2.getTaskList().get(1).getTaskName().equals("Java task");
    }

    @Test
    void repoUpdateTest() {
        repo.deleteAll();
        User user = new User("Name", "pass", true);
        repo.save(user);
        for (var task: list) {
            user.addTask(task);
        }
        assert user.deleteTask(user.getTaskList().get(2));
        assert user.getTaskList().size() == 2;
        User user2 = new User("Name2", "pass2", true);
        user2.setId(user.getId());
        user2.addTask(list.get(2));
        repo.save(user2);
        assert repo.existsById(user2.getId());
        assert repo.findById(user2.getId()).isPresent();
        user = repo.findById(user2.getId()).get();
        assert user.getName().equals(user2.getName());
        assert user.getPassword().equals(user2.getPassword());
        List<UserTask> tasks = user.getTaskList();
        int i = 0;
        for (var task: tasks) {
            assert task.getTaskName().equals(user2.getTaskList().get(i).getTaskName());
            i++;
        }
    }

    @Test
    void repoDeleteTest() {
        repo.deleteAll();
        User user = new User("Name", "pass", true);
        repo.save(user);
        assert repo.existsById(user.getId());
        repo.delete(user);
        assert !repo.existsById(user.getId());
        repo.save(user);
        repo.deleteById(user.getId());
        assert !repo.existsById(user.getId());
    }
}
