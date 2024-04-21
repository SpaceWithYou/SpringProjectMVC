package com.example.project.entity;

import com.example.project.util.TaskProblem;
import com.example.project.util.TaskProblemConverter;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**User task group*/
@Table(
        name = "tasks",
        indexes = @Index(name = "idx_user_id", columnList = "user_id")
)
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserTask {
    private String taskName;

    @Id private UUID id = UUID.randomUUID();

    @Transient private static final int GROUP_MAX_QUESTIONS = 5;
    /**
     * Group of  questions (max - @see GROUP_MAX_QUESTIONS) <br>
     * Question with answers and right answers <br>
     * In JSON
     */
    @Column(name = "problems", columnDefinition = "text")
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "task_problems",
            indexes = @Index(name = "idx_task", columnList = "problems")
    )
    @Convert(converter = TaskProblemConverter.class)
    private List<TaskProblem> problem;

    @JoinColumn(referencedColumnName = "user_tasks", name = "user_id")
    @Column(name = "user_id")
    private long userId;

    public UserTask(String TaskName, long userId, List<TaskProblem> problem) {
        if(TaskName.isEmpty() || userId < 0) throw new IllegalArgumentException();
        this.taskName = TaskName;
        this.userId = userId;
        this.problem = new ArrayList<>(problem);
    }

    public UserTask(String TaskName, long userId) {
        if(TaskName.isEmpty() || userId < 0) throw new IllegalArgumentException();
        this.taskName = TaskName;
        this.userId = userId;
        this.problem = new ArrayList<>();
    }

    public UserTask(UserTask task) {
        this.taskName = task.getTaskName();
        this.problem = new ArrayList<>(task.getProblem());
        this.userId = task.getUserId();
    }

    public void addProblemTask(TaskProblem task) {
        if(problem.size() < GROUP_MAX_QUESTIONS) this.problem.add(task);
    }

    public void removeProblemTask(TaskProblem task) {
        this.problem.remove(task);
    }
}
