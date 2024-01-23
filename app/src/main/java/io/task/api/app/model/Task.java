package io.task.api.app.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("context")
    String context;

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

    @JsonProperty("recur")
    String frequency;

    @JsonProperty("until")
    String until;
 
    @JsonProperty("depends")
    String depends;


    
    public String getDepends() {
        return depends;
    }

    public void setDepends(String depends) {
        this.depends = depends;
    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", description=" + description + ", start=" + start + ", end=" + end + ", due=" + due
                + ", entry=" + entry + ", modified=" + modified + ", tags=" + tags + ", project=" + project
                + ", status=" + status + ", uuid=" + uuid + ", score=" + score + ", effort=" + effort + ", urgency="
                + urgency + "]";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getEffort() {
        return effort;
    }

    public void setEffort(String effort) {
        this.effort = effort;
    }

    public Float getUrgency() {
        return urgency;
    }

    public void setUrgency(Float urgency) {
        this.urgency = urgency;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

}
