package io.task.api.app.utils;

public class TaskApiAppError {
    private String message;
    private long timestamp;

    public TaskApiAppError(String message, long timestamp) {
        super();
        this.message = message;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "GalleryAppError [message=" + message + ", timestamp=" + timestamp + "]";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


}
