package com.bujdi.carRecords.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        Map<String, Map<String, String>> response = new HashMap<>();
        response.put("errors", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(S3Exception.class)
    protected ResponseEntity<Object> handleS3Exception(S3Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SdkException.class)
    protected ResponseEntity<Object> handleSdkException(SdkException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
    }
    @ExceptionHandler({IOException.class, FileNotFoundException.class, MultipartException.class})
    protected ResponseEntity<Object> handleIOException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @Value("${server.debugEnabled}")
    private boolean debugEnabled;

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleUnExpectedException(Exception ex, WebRequest request) {
        if (debugEnabled) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
        return new ResponseEntity<>("Something went wrong", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}