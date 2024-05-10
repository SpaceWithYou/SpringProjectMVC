package com.example.project.entity;

import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@NoArgsConstructor
public class AnswerKey implements Serializable {
    private long userId;
    private UUID taskId;
    private int problemNum;
}
