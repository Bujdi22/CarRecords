package com.bujdi.carRecords.service;

import com.bujdi.carRecords.exception.FileDownloadException;
import com.bujdi.carRecords.exception.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    String uploadFile(MultipartFile multipartFile) throws FileUploadException, IOException;

    InputStream downloadFile(String fileName) throws FileDownloadException, IOException;

    void delete(String fileName);
}
