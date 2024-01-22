package io.task.api.app.controller;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.task.api.app.model.Task;
import io.task.api.app.servicemetier.TaskServiceMetier;
import io.task.api.app.utils.AppConstants;
import io.task.api.app.utils.TaskApiException;

@RestController
public class TaskController {

    @Autowired 
    TaskServiceMetier taskServiceMetier;
    private static final Logger LOGGER = Logger.getLogger(TaskUserController.class.getName());
    
    @GetMapping(AppConstants.TASK_PATH)
    public ResponseEntity<Task> retreiveTask(@PathVariable String id) throws InterruptedException{
        LOGGER.log(Level.INFO, "Task requested"+id);
        Task response = taskServiceMetier.findTaskById(id);
        return new ResponseEntity<Task>(response, HttpStatus.OK );
    }

    @GetMapping(AppConstants.PROJECT_PATH)
    public ResponseEntity<List<Task>> retreiveTasksByProject(@PathVariable String projectName) throws InterruptedException{
        LOGGER.log(Level.INFO, "Task requested"+projectName);
        List<Task> response = taskServiceMetier.findTasksByProject(projectName);
        return new ResponseEntity<List<Task>>(response, HttpStatus.OK );
    }

    @GetMapping(AppConstants.ALL_TASKS_PATH)
    public ResponseEntity<List<Task>> retreiveAllTasks() throws InterruptedException{
        LOGGER.log(Level.INFO, "All tasks requested");
        List<Task> response = taskServiceMetier.getAllTasks();
        return new ResponseEntity<List<Task>>(response, HttpStatus.OK );
    }

    @GetMapping(AppConstants.ACTIVE_TASKS_PATH)
    public ResponseEntity<List<Task>> retreiveActiveTasks() throws InterruptedException{
        LOGGER.log(Level.INFO, "Active tasks requested");
        List<Task> response = taskServiceMetier.getAllTasks();
        return new ResponseEntity<List<Task>>(response, HttpStatus.OK );
    }

    @ExceptionHandler
    public ResponseEntity<TaskApiException> handleException(AccessDeniedException e) {
        LOGGER.info(e.getMessage());

        TaskApiException response = new TaskApiException(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<TaskApiException>(response, HttpStatus.FORBIDDEN);

    }
}
