package com.example.project.repository;

import com.example.project.entity.AnswerKey;
import com.example.project.entity.UserAnswer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserAnswerRepository extends CrudRepository<UserAnswer, AnswerKey> {

    //get All user answers
    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Query(value = "select * from user_answers ans where ans.user_id = :Id", nativeQuery = true)
    Iterable<UserAnswer> findAllUserAnswersByUserId(@Param("Id") long userId);
}
