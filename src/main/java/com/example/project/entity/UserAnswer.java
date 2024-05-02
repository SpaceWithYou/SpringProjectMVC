package com.example.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**User answers class*/
@Entity
@Table(
        name = "answers",
        indexes = {
                @Index(name = "answer_user_idx", columnList = "user_id"),
                @Index(name = "answer_task_idx", columnList = "task_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(AnswerKey.class)
public class UserAnswer {

    @Column(name = "answers", columnDefinition = "text")
    private List<String> answers;

    @Id
    @Column(name = "user_id")
    private long userId;

    @Id
    @Column(name = "task_id")
    private UUID taskId;

    @Column(name = "time")
    private Date date;
}
