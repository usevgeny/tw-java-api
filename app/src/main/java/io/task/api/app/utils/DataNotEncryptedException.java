package io.task.api.app.utils;

public class DataNotEncryptedException extends RuntimeException{

    public DataNotEncryptedException(String message) {
        super(message);
    }

}
