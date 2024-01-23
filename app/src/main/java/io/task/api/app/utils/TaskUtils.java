package io.task.api.app.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.task.api.app.model.Task;

public class TaskUtils {

    public List<Task> stringToListOfTasks(String receivedInput) throws TaskApiException, JsonMappingException {
        receivedInput = receivedInput.replace("\n", "").trim();
        String jsonString = extractJsonSubstring(receivedInput);
//        return  parseJson(jsonString, Task.class);
        List<Task> tasks = new ArrayList<>();
        try {
            tasks = parseListOfTasks(jsonString);
        } catch (JsonProcessingException e) {
            throw new TaskApiException("Json parsing error");
        }
        return tasks;
    }

    public String extractJsonSubstring(String input) {
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

    public static List<Task> parseListOfTasks(String jsonString) throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, new TypeReference<List<Task>>() {
        });
    }

    public String getTaskCommand(Task task) {
                return String.format(TaskParams.ADD_TASK.getValue()
                .concat(" ")
                .concat(TaskParams.PARAM_PROJECT.getValue()).concat(task.getProject())
                .concat(" ")
                .concat(TaskParams.PARAM_CONTEXT.getValue()).concat(task.getContext())
                .concat(" ")
                .concat(TaskParams.PARAM_PRIORITY.getValue()).concat(task.getPriority())
                .concat(" ")
                .concat(TaskParams.PARAM_EFFORT.getValue()).concat(task.getEffort())
                .concat(" ")
                .concat(TaskParams.PARAM_SCORE.getValue()).concat(String.valueOf(task.getScore()))
                .concat(" ")
                .concat(TaskParams.PARAM_START.getValue()).concat(String.valueOf(task.getStart()))
                .concat(" ")
                .concat(TaskParams.PARAM_DUE.getValue()).concat(String.valueOf(task.getDue()))
                .concat(" ")
                .concat(TaskParams.PARAM_RECUR.getValue()).concat(String.valueOf(task.getFrequency()))
                .concat(" ")
                .concat(TaskParams.PARAM_UNTIL.getValue()).concat(String.valueOf(task.getUntil()))
                .concat(" ")
                .concat(TaskParams.PARAM_DEPENDS.getValue()).concat(String.valueOf(task.getDepends())));
    }
}
