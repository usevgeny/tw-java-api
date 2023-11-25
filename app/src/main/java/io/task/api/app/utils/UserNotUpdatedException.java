package io.task.api.app.utils;

public class UserNotUpdatedException extends RuntimeException{

    public UserNotUpdatedException(String message) {
        super(message);
    }

}
