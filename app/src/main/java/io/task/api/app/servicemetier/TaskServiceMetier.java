package io.task.api.app.servicemetier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import app.mutlithread.enums.WorkType;
import app.mutlithread.service.WorkManager;
import io.task.api.app.model.Task;
import io.task.api.app.service.TaskService;
import io.task.api.app.utils.TaskApiException;

@Service
public class TaskServiceMetier {

    @Value("${multithread.number}")
    private Integer threads;

    @Value("${task.source}")
    private String taskSource;

    @Value("${task.password}")
    private String password;

    @Value("${task.recipient}")
    private String recipient;

    @Autowired
    TaskService taskService;

    private WorkManager workManager = new WorkManager();

    public Task findTaskById(String id) throws InterruptedException {
        Task task;
        try {

            workManager.processFileMultiThread(threads, taskSource, WorkType.DECRYPT, password, recipient);
            task = taskService.getTaskById(id);

            return task;
        } catch (JsonProcessingException | TaskApiException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            workManager.processFileMultiThread(threads, taskSource, WorkType.ENCRYPT, password, recipient);
        }
        return null;
    }

    public List<Task> findTasksByProject(String projectName) throws InterruptedException {
        try {

            workManager.processFileMultiThread(threads, taskSource, WorkType.DECRYPT, password, recipient);
            List<Task> tasks = taskService.getTasksByProject(projectName);

            return tasks;
        } catch (JsonProcessingException | TaskApiException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            workManager.processFileMultiThread(threads, taskSource, WorkType.ENCRYPT, password, recipient);
        }
        return null;
    }

    public List<Task> getAllTasks() throws InterruptedException {
        try {

            workManager.processFileMultiThread(threads, taskSource, WorkType.DECRYPT, password, recipient);
            List<Task> tasks = taskService.getAllTasks();

            return tasks;
        } catch (JsonProcessingException | TaskApiException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            workManager.processFileMultiThread(threads, taskSource, WorkType.ENCRYPT, password, recipient);
        }
        return null;
    }

}
