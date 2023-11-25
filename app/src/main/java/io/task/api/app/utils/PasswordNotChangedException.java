package io.task.api.app.utils;

public class PasswordNotChangedException extends RuntimeException {

    public PasswordNotChangedException(String message) {
        super(message);
    }
}
