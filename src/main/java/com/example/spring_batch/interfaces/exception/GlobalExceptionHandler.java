package com.example.spring_batch.interfaces.exception;

import com.example.spring_batch.interfaces.dto.ApiResponse;
import com.example.spring_batch.shared.exception.BatchJobException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle BatchJobException
     */
    @ExceptionHandler(BatchJobException.class)
    public ResponseEntity<ApiResponse<String>> handleBatchJobException(
            BatchJobException ex, WebRequest request) {
        
        log.error("Batch job exception: {}", ex.getMessage(), ex);
        
        ApiResponse<String> response = ApiResponse.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        
        log.error("Illegal argument exception: {}", ex.getMessage(), ex);
        
        ApiResponse<String> response = ApiResponse.error("Invalid argument: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle generic exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(
            Exception ex, WebRequest request) {
        
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        
        ApiResponse<String> response = ApiResponse.error("An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
} 