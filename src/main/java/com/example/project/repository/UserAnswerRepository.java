package com.example.project.repository;

import com.example.project.entity.AnswerKey;
import com.example.project.entity.UserAnswer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends CrudRepository<UserAnswer, AnswerKey> {

    //get All user answers
    //@Query(value = "select * from user_answers ans where ans.user_id = :Id", nativeQuery = true)
    @Query("ans from user_answers where ans.user_id = :Id")
    List<UserAnswer> getAnswersByUserId(@Param("Id") long userId);
}
