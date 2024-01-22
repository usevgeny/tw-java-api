package io.task.api.app.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {
    @JsonProperty("id")
    String id;
    @JsonProperty("description")
    String description;
    @JsonProperty("start")
    String start;
    @JsonProperty("end")
    String end;
    @JsonProperty("due")
    String due;
    @JsonProperty("entry")
    String entry;
    @JsonProperty("modified")
    String modified;
    @JsonProperty("tags")
    List<String> tags;
    @JsonProperty("project")
    String project;
    @JsonProperty("status")
    String status;
    @JsonProperty("uuid")
    String uuid;
    @JsonProperty("score")
    Float score;
    @JsonProperty("priority")
    String priority;
    @JsonProperty("effort")
    String effort;
    @JsonProperty("urgency")
    Float urgency;
    @Override
    public String toString() {
        return "Task [id=" + id + ", description=" + description + ", start=" + start + ", end=" + end + ", due=" + due
                + ", entry=" + entry + ", modified=" + modified + ", tags=" + tags + ", project=" + project
                + ", status=" + status + ", uuid=" + uuid + ", score=" + score + ", effort=" + effort + ", urgency="
                + urgency + "]";
    }


}
