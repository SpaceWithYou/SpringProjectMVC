package com.example.project.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.util.ArrayList;
import java.util.List;

/**User class*/
@Table(name = "users")
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    private String name;

    private String password;                                                                    //Hashed password
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;                                                                            //Ignore in constructor

    private boolean isSuperUser = false;

    @Column(name = "user_tasks")
    @OneToMany(mappedBy = "userId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<UserTask> taskList;

    public User(String name, String pass, boolean isSuperUser) {
        this.name = name;
        this.password = pass;
        this.isSuperUser = isSuperUser;
        taskList = new ArrayList<>();
    }

    public User(String name, String pass, boolean isSuperUser, List<UserTask> tasks) {
        this.name = name;
        this.password = pass;
        this.isSuperUser = isSuperUser;
        taskList = new ArrayList<>();
        for (UserTask task: tasks) {
            if(task.getUserId() == this.id) {
                taskList.add(task);
            }
        }
    }

    public void addTask(UserTask task) {
        if(task.getUserId() == this.id) taskList.add(task);
    }

    public boolean deleteTask(UserTask task) {
        if(task.getUserId() == this.id) return taskList.remove(task);
        else return false;
    }
}
