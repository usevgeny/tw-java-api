package io.task.api.app.servicemetier;

import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import io.task.api.app.DTO.TaskUserMiddleDTO;
import io.task.api.app.model.TaskUser;
import io.task.api.app.service.TaskUserService;
import io.task.api.app.utils.TaskUserValidator;

@Component
public class TaskUserManagementService {
    
    @Autowired
    private TaskUserValidator taskUserValidator;
    @Autowired
    private TaskUserService taskUserService;

    public HttpStatus createNewUser(TaskUser appuser) {

        taskUserValidator.validate(appuser, bindingResult);
        // TODO appuserRolesValidator validatevalidate(appuserrole, bindingResult)

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError e : errors) {
                errorMessage.append(e.getDefaultMessage()).append(";");
            }
            throw new UserNotCreatedException(errorMessage.toString());
        }


        taskUserService.createTaskUser(appuser);
    }
}
