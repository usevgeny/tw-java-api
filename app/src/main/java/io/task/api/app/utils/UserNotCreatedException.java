package io.task.api.app.utils;

public class UserNotCreatedException extends RuntimeException{

    public UserNotCreatedException(String message) {
        super(message);
    }

}
