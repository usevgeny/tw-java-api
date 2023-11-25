package io.task.api.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.task.api.app.model.TaskUser;

public interface TaskUserRepository extends JpaRepository<TaskUser, Integer>{

    Optional<TaskUser> findOneByUserName(String username);
    Optional<TaskUser> findOneByUserEmail(String userEmail);
}
