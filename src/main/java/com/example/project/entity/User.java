package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

/**User class*/
@Table(name = "users")
@Entity
@Data
public class User {
    private String name;

    private String password;        //Hashed password
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private boolean isSuperUser = false;

    @Column(name = "user_tasks")
    @OneToMany(mappedBy = "user_id")
    private List<UserTask> taskList;

    public void addTask(UserTask task) {
        taskList.add(task);
    }

    public boolean deleteTask(UserTask task) {
        return taskList.remove(task);
    }
}
