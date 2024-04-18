package com.example.project.entity;

import com.example.project.util.TaskProblem;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

/**User task group*/
@Table(
        name = "tasks",
        indexes = @Index(name = "idx_user_id", columnList = "user_id")
)
@Entity
@Data
public class UserTask {
    private String name;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private static final int GROUP_MAX_QUESTIONS = 5;
    /**
     * Group of  questions (max - @see GROUP_MAX_QUESTIONS) <br>
     * Question with answers and right answers
     */
    private List<TaskProblem> problem;
    @JoinColumn(referencedColumnName = "user_tasks") private long user_id;
}
