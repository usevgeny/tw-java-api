package io.task.api.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import io.task.api.app.model.TaskUserRole;

public interface TaskUserrolesRepository extends JpaRepository<TaskUserRole, Integer> {

    Optional<TaskUserRole> findOneByRoleTitle(String roleTitle);
}
