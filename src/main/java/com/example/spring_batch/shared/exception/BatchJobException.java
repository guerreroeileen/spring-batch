package com.example.spring_batch.shared.exception;

public class BatchJobException extends RuntimeException {

    public BatchJobException(String message) {
        super(message);
    }

    public BatchJobException(String message, Throwable cause) {
        super(message, cause);
    }
} 