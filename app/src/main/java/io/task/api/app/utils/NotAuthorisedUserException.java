package io.task.api.app.utils;

public class NotAuthorisedUserException extends RuntimeException {

    public NotAuthorisedUserException(String message) {
        super(message);
    }
}
