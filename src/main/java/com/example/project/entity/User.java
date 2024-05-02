package com.example.project.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**User class*/
@Table(name = "users")
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class User {
    @Column(unique = true)
    private String name;

    private String password;                                                                    //Hashed password
    @Id
    @SequenceGenerator(name = "id_generator", sequenceName = "id_sequence", initialValue = 1, allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "id_generator")
    @Column(name = "id", insertable = false, updatable = false)
    private long id;

    private boolean isSuperUser = false;

    @Column(name = "user_tasks")
    @OneToMany(mappedBy = "userId", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserTask> taskList;

    public User(String name, String pass, boolean isSuperUser) {
        if(name.isEmpty()) throw new IllegalArgumentException();
        this.name = name;
        this.password = pass;
        this.isSuperUser = isSuperUser;
        taskList = new ArrayList<>();
    }

    public User(String name, String pass, boolean isSuperUser, List<UserTask> tasks) {
        if(name.isEmpty()) throw new IllegalArgumentException();
        this.name = name;
        this.password = pass;
        this.isSuperUser = isSuperUser;
        taskList = new ArrayList<>();
        for (UserTask task: tasks) {
            this.addTask(task);
        }
    }
    /**Adds new Tasks, if task have different userID, change it to current user id**/
    public void addTask(UserTask task) {
        UserTask newTask = new UserTask(task);
        newTask.setUserId(this.id);
        taskList.add(newTask);
    }
    /**Deletes task if it has current user id**/
    public boolean deleteTask(UserTask task) {
        if(task.getUserId() == this.id) return taskList.remove(task);
        else return false;
    }
    /**Deletes task by id*/
    public boolean deleteTask(UUID taskId) {
        for (int i = 0; i < taskList.size(); i++) {
            if(taskList.get(i).getId() == taskId) {
                taskList.remove(i);
                return true;
            }
        }
        return false;
    }
}
