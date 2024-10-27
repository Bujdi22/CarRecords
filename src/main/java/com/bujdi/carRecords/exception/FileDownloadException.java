package com.bujdi.carRecords.exception;

public class FileDownloadException extends SpringBootFileUploadException {
    public FileDownloadException(String message) {
        super(message);
    }
}