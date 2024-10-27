package com.bujdi.carRecords.exception;

public class FileUploadException extends SpringBootFileUploadException{
    public FileUploadException(String message) {
        super(message);
    }
}
