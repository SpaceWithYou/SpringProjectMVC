package com.example.project.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**Task problem*/
@Getter
@Setter
@AllArgsConstructor
public class TaskProblem {

    private String name;
    /**
     * Answer map, right answer have true value, otherwise - false <br>
     * One answer must be correct
     * */
    private Map<String, Boolean> answerMap;

    public TaskProblem(String name) {
        this.name = name;
    }

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
}
