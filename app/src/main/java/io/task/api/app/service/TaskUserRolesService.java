package io.task.api.app.service;

import javax.management.relation.RoleNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.task.api.app.model.TaskUserRole;
import io.task.api.app.repository.TaskUserrolesRepository;

@Service
public class TaskUserRolesService {
    
    private final TaskUserrolesRepository taskUserRolesRepository;

    @Autowired
    public TaskUserRolesService(TaskUserrolesRepository appuserrolesRepository) {
        super();
        this.taskUserRolesRepository = appuserrolesRepository;
    }
    
    public TaskUserRole getRoleByName(String roleTitle) throws RoleNotFoundException {
        return taskUserRolesRepository.findOneByRoleTitle(roleTitle).orElseThrow(RoleNotFoundException::new);
    }
    

}
