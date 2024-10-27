package com.bujdi.carRecords.exception;

public class FileEmptyException extends SpringBootFileUploadException {
    public FileEmptyException(String message) {
        super(message);
    }
}
