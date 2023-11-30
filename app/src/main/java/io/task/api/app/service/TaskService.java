package io.task.api.app.service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.task.api.app.model.Task;
import io.task.api.app.utils.TaskApiAppError;

public class TaskService {

    private final String PARAM_EXPORT = " export";
    // Specifies High, Medium, Low and no priority for a task.
    private final String PARAM_PRIORITY = "priority:%s";// H|M|L or priority:

//Specifies the frequency of a recurrence of a task.
    private final String PARAM_RECUR = "recur:%s";// <frequency>

//Specifies the date after which a task can be accomplished.
    private final String PARAM_SCHEDULED = "scheduled:%s";// <ready-date>

//Specifies the expiration date of a task, after which it will be deleted.
    private final String PARAM_UNTIL = "until:%s";// <expiration date of task>

//When  a  task  is given a wait date, it is hidden from most built-in reports, which exclude +WAITING.  When
//compatibilty, such tasks are shown as having status "waiting", but this will change in a future release.
    private final String PARAM_WAIT = "wait:%s"; // <wait-date>
//Declares  this  task  to be dependent on id1 and id2.  This means that the tasks id1 and id2 should be com‐
//comma-separated list of ID numbers, UUID numbers and ID ranges.  When prefixing any element of this list by
//pleted before this task.  Consequently, this task will then show up on the 'blocked' report.  It accepts  a
//'-', the specified tasks are removed from the dependency list.
    private final String PARAM_DEPENDS = "depends:%d";// <id1,id2 ...>
//For report purposes, specifies the date that a task was created.
    private final String PARAM_ENTRY = "entry:%s";// <entry-date>

    private final String PARAM_PROJECT = "project:%s";
    private final String PARAM_EFFORT = "effort:%d";
    private final String PARAM_SCORE = "score:%s";
    // finish date
    private final String PARAM_DUE = "due:%s";
    private final String PARAM_START = "start";
    private final String PARAM_DONE = "done";
    // waits until a specific date before showing in a backlog
    private final String ADD_TASK = "add";

    public String executeCommand(String args, String input) throws TaskApiAppError {
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
            processBuilder.environment().put("TASKDATA", "/home/evgeny/PROJECTS/TaskApi/tests/");
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
            throw new TaskApiAppError("Task command error" + e.getMessage());
        }
    }

    private Task getTaskById(String id) throws JsonMappingException, JsonProcessingException, TaskApiAppError {
        String query = id.concat(PARAM_EXPORT);
        List<Task> singleTaskLisk = stringToListOfTasks(executeCommand(query, null));
        if (!singleTaskLisk.isEmpty()){
            return singleTaskLisk.get(0);
        }
        return null;
    };

    private List<Task> getTasksByProject(String projectName) throws JsonMappingException, JsonProcessingException, TaskApiAppError {
        String query = String.format((PARAM_PROJECT), projectName).concat(PARAM_EXPORT);
        return stringToListOfTasks(executeCommand(query, null));
    }

    private List<Task> findTasks(String query) {
        return null;
    }

    private List<Task> stringToListOfTasks(String receivedInput) throws JsonMappingException, JsonProcessingException {
        receivedInput = receivedInput.replace("\n", "").trim();
        String jsonString = extractJsonSubstring(receivedInput);
//        return  parseJson(jsonString, Task.class);
        return  parseListOfTasks(jsonString);
    }

    private String extractJsonSubstring(String input) {
        List<String> patterns = List.of("\\[.*?\\}]", "\\[.*?\\]", "\\{.*?\\}");
        for (String pattern : patterns) {
            Pattern jsonPattern = Pattern.compile(pattern);
            Matcher matcherList = jsonPattern.matcher(input);
            if (matcherList.find()) {
                return matcherList.group(0);
            }
        }
        return null;
    }
    
    public static List<Task> parseListOfTasks (String jsonString) throws JsonMappingException, JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, new TypeReference<List<Task>>(){});
    }

//    public static <T> List<T> parseJson(String jsonString, Class<T> valueType) throws JsonMappingException, JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//                return objectMapper.readValue(jsonString, new TypeReference<List<T>>(){});
//        }

    public static void main(String[] args) throws JsonMappingException, JsonProcessingException {
        TaskService handler = new TaskService();

        try {
            String result = handler.executeCommand("export", null);
            System.out.println(handler.stringToListOfTasks(result));
//            System.out.println(handler.getTaskById("2"));
//            System.out.println(handler.getTasksByProject("MidJourney"));
        } catch (TaskApiAppError e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
