package com.example.project;

import com.example.project.entity.AnswerKey;
import com.example.project.entity.UserAnswer;
import com.example.project.repository.UserAnswerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest(properties = {"spring.config.location=classpath:application-test.properties"})
public class UserAnswerRepoTests {
    @Autowired
    private UserAnswerRepository repo;

    private UserAnswer prepareData() {
        List<String> answers = new ArrayList<>();
        answers.add("2");
        answers.add("a");
        answers.add("b");
        answers.add("-1");
        UUID id = UUID.randomUUID();
        return new UserAnswer(answers, 5, 1, id, LocalDateTime.now());
    }

    @Test
    void repoSaveTest() {
        UserAnswer ans = prepareData();
        repo.save(ans);
        assert repo.existsById(new AnswerKey(1, ans.getTaskId(), 5));
    }

    @Test
    void repoGetTest() {
        UserAnswer answer = prepareData();
        repo.save(answer);
        Optional<UserAnswer> optAnswer = repo.findById(new AnswerKey(1, answer.getTaskId(), 5));
        assert optAnswer.isPresent();
        UserAnswer saved = optAnswer.get();
        assert saved.getUserId() == answer.getUserId();
        assert saved.getProblemNum() == answer.getProblemNum();
        //date with timezone in class, without in database
        assert saved.getDate().equals(answer.getDate());
        assert saved.getTaskId() == answer.getTaskId();
        assert saved.getAnswers().equals(answer.getAnswers());
    }

    @Test
    void repoUpdateTest() {
        UserAnswer answer = prepareData();
        repo.save(answer);
        AnswerKey key = new AnswerKey(1, answer.getTaskId(), 5);
        assert repo.existsById(key);
        answer.getAnswers().add("0");
        answer.getAnswers().remove("b");
        UserAnswer newAnswer = new UserAnswer(answer.getAnswers(), 5, 1, answer.getTaskId(), LocalDateTime.now());
        repo.save(newAnswer);
        assert repo.existsById(key);
    }

    @Test
    void repoDeleteTest() {
        UserAnswer answer = prepareData();
        AnswerKey key = new AnswerKey(1, answer.getTaskId(), 5);
        repo.save(answer);
        repo.delete(answer);
        assert !repo.existsById(key);
        repo.save(answer);
        repo.deleteById(key);
        assert !repo.existsById(key);
    }

    @Test
    void repoGetAnswersByUserId() {
        UserAnswer answer = prepareData();
        repo.save(answer);
        UserAnswer answer2 = prepareData();
        answer2.setProblemNum(3);
        repo.save(answer2);
        UserAnswer answer3 = prepareData();
        answer3.setTaskId(UUID.randomUUID());
        repo.save(answer3);
        UserAnswer answer4 = prepareData();
        answer4.setDate(LocalDateTime.now());
        repo.save(answer4);
        UserAnswer answer5 = prepareData();
        List<String> newAnswers = new ArrayList<>();
        newAnswers.add("0");
        newAnswers.add("1");
        newAnswers.add("2");
        newAnswers.add("a");
        newAnswers.add("b");
        newAnswers.add("c");
        newAnswers.add("-");
        answer5.setAnswers(newAnswers);
        repo.save(answer5);
        Iterable<UserAnswer> answers = repo.findAllUserAnswersByUserId(answer.getUserId());
        assert answers.iterator().hasNext();
    }
}
