package com.example.project.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.util.Map;

/**Task problem*/
@Getter
@Setter
@RequiredArgsConstructor
public class TaskProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;
    /**
     * Answer map, right answer have true value, otherwise - false*/
    Map<String, Boolean> answerMap;

    public void addAnswer(String ans, boolean isRight) {
        answerMap.put(ans, isRight);
    }

    public void changeAnswer(String ans, boolean isRight) {
        answerMap.replace(ans, isRight);
    }

    public void deleteAnswer(String ans) {
        answerMap.remove(ans);
    }

    public boolean isRight(String ans) {
        return answerMap.getOrDefault(ans, false);
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

}
