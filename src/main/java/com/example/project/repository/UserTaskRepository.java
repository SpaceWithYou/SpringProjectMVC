package com.example.project.repository;

import com.example.project.entity.UserTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserTaskRepository extends CrudRepository<UserTask, UUID> {
}
