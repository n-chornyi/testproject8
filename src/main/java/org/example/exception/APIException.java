package org.example.exception;

public abstract class APIException extends RuntimeException{

    public APIException(String message) {
        super(message);
    }
}
