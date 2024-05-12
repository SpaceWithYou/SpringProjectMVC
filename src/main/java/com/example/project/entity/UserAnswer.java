package com.example.project.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**User answers class for 1 problem task*/
@Entity
@Table(name = "user_answers")
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@IdClass(AnswerKey.class)
public class UserAnswer {

    @ElementCollection
    @CollectionTable(name = "answers",
            joinColumns = {
                    @JoinColumn(name = "problem_num",referencedColumnName = "problem_num"),
                    @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
                    @JoinColumn(name = "task_id", referencedColumnName = "task_id")
            })
    @Column(name = "answer")
    private List<String> answers;

    @Id
    @Column(name = "problem_num")
    private int problemNum = 0;

    @Id
    @Column(name = "user_id")
    private long userId;

    @Id
    @Column(name = "task_id")
    private UUID taskId;

    @Column(name = "time")
    private LocalDateTime date;
}
