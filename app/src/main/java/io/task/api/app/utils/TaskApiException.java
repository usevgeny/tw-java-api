package io.task.api.app.utils;

public class TaskApiException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private String message;
    private long timestamp;

    public TaskApiException(String message, long timestamp) {
        super();
        this.message = message;
        this.timestamp = timestamp;
    }

    public TaskApiException(String message) {
        // TODO Auto-generated constructor stub
        this.message = message;
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
