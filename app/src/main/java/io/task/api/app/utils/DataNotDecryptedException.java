package io.task.api.app.utils;

public class DataNotDecryptedException extends RuntimeException{

    public DataNotDecryptedException(String message) {
        super(message);
    }

}
