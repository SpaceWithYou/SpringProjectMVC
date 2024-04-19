package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @Transient private static final int GROUP_MAX_QUESTIONS = 5;
    /**
     * Group of  questions (max - @see GROUP_MAX_QUESTIONS) <br>
     * Question with answers and right answers <br>
     * In JSON
     */
    @Column(columnDefinition = "json")
    private String problem;

    @JoinColumn(referencedColumnName = "user_tasks") private long user_id;
}
