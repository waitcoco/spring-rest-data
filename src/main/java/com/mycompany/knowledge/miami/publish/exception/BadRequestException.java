package com.mycompany.knowledge.miami.publish.exception;

public class BadRequestException extends RuntimeException {
    private final String message;

    public BadRequestException(String message) {
        this.message = message;
    }

    public BadRequestException(Exception e) {
        this(e.getMessage());
    }

    public String getMessage() {
        return message;
    }
}
