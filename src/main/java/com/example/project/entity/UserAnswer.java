package com.example.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**User answers class for 1 problem task*/
@Entity
@Table(name = "user_answers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AnswerKey.class)
public class UserAnswer {

    @Column(name = "answers", columnDefinition = "text")
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
    private Date date;
}
