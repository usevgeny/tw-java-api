package io.task.api.app.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.task.api.app.model.Task;
import io.task.api.app.utils.TaskApiException;
import io.task.api.app.utils.TaskParams;
import io.task.api.app.utils.TaskUtils;

@Service
public class TaskService {

    private static final Logger LOGGER = Logger.getLogger(PersonDetailsService.class.getName());

    @Autowired
    TaskUtils taskUtils;

    @Value("${task.source}")
    private String taskSource;


    public String executeCommand(String args, String input) throws TaskApiException {
        try {
            // Construct the Taskwarrior command
//            String command = String.format(
//                    "task rc.confirmation=no rc.recurrence.confirmation=no rc.dependency.confirmation=no rc.json.depends.array=yes rc.bulk=0 %s",
//                    args);
            String command = String.format(
                    "task %s",
                    args);

            // Use ProcessBuilder to execute the command
            ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
            processBuilder.redirectErrorStream(true);

            // Set encoding, max buffer, and optional input
            processBuilder.environment().put("LANG", "en_US.UTF-8");
            processBuilder.environment().put("TASKDATA", taskSource);
            processBuilder.redirectErrorStream(true);
            processBuilder.redirectInput(ProcessBuilder.Redirect.PIPE);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
            processBuilder.redirectError(ProcessBuilder.Redirect.PIPE);

            // Start the process
            Process process = processBuilder.start();

            // Provide optional input to the command
            if (input != null) {
                process.getOutputStream().write(input.getBytes());
                process.getOutputStream().flush();
                process.getOutputStream().close();
            }

            // Read the output of the command
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                StringBuilder output = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                return output.toString().trim();
            }

        } catch (IOException e) {
            throw new TaskApiException("Task command error" + e.getMessage());
        }
    }

    public Task getTaskById(String id) throws JsonMappingException, JsonProcessingException, TaskApiException {
        String query = id.concat(TaskParams.PARAM_EXPORT.getValue());
        List<Task> singleTaskLisk = taskUtils.stringToListOfTasks(executeCommand(query, null));
        if (!singleTaskLisk.isEmpty()) {
            return singleTaskLisk.get(0);
        }
        return null;
    };

    public List<Task> getTasksByProject(String projectName)
            throws JsonMappingException, JsonProcessingException, TaskApiException {
        String query = String.format((TaskParams.PARAM_PROJECT.getValue()), projectName)
                .concat(TaskParams.PARAM_EXPORT.getValue());
        return taskUtils.stringToListOfTasks(executeCommand(query, null));
    }

    public List<Task> getAllTasks() throws JsonMappingException, TaskApiException {
        String query = TaskParams.PARAM_EXPORT.getValue().concat(" ").concat(TaskParams.ALL_TASKS.getValue());
        return taskUtils.stringToListOfTasks(executeCommand(query, null));
    }

    public List<Task> getAllActive() throws JsonMappingException, TaskApiException {
        String query = TaskParams.PARAM_EXPORT.getValue().concat(" ").concat(TaskParams.ACTIVE_TASKS.getValue());
        return taskUtils.stringToListOfTasks(executeCommand(query, null));
    }

    public Task addNewTask(Task newTask) {
        String query = TaskParams.ADD_TASK.getValue().concat(taskUtils.getTaskCommand(newTask));
        try {
            executeCommand(query, null);
            return newTask;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Task creation failed: "+e.getMessage());
        }
            return null;
    }

    public Task updateTask(Task taskToUpdate) {
        String query = taskToUpdate.getId().concat(" ").concat(TaskParams.EDIT_TASK.getValue()).concat(taskUtils.getTaskCommand(taskToUpdate));
        try {
            executeCommand(query, null);
            return taskToUpdate;
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Task update failed: "+e.getMessage());
        }
            return null;
    }

    public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
        TaskService handler = new TaskService();

        try {
            String result = handler.executeCommand("export", null);
//            System.out.println(taskJsonUtils.stringToListOfTasks(result));
            System.out.println(handler.getTaskById("35"));
//            System.out.println(handler.getTasksByProject("MidJourney"));
        } catch (TaskApiException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
