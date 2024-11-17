package com.bujdi.carRecords.service;

import com.bujdi.carRecords.exception.FileDownloadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    @Value("${aws.bucket.name}")
    private String bucketName;

    private final S3Client s3Client;

    @Autowired
    private CompressionService compressionService;

    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        // Generate file name
        String fileName = generateFileName(multipartFile);

        String contentType = multipartFile.getContentType();

        if ("image/jpeg".equals(contentType) || "image/png".equals(contentType)) {
            multipartFile = compressionService.compressImage(multipartFile);
        }

        // Create metadata
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", "plain/" + FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        metadata.put("Title", "File Upload - " + fileName);

        // Create PutObjectRequest with metadata and file content
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .metadata(metadata)
                .build();

        // Upload file directly from MultipartFile bytes
        PutObjectResponse response = s3Client.putObject(
                putObjectRequest,
                RequestBody.fromBytes(multipartFile.getBytes())
        );

        return fileName;
    }
    @Override
    public InputStream downloadFile(String fileName) throws FileDownloadException, IOException {
        if (bucketIsEmpty()) {
            throw new FileDownloadException("Requested bucket does not exist or is empty");
        }

        // Set up request to download the file
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        try {
            // Attempt to get the object from S3
            ResponseInputStream<GetObjectResponse> s3is = s3Client.getObject(getObjectRequest);

            // Check if the stream is empty or not
            if (s3is == null) {
                throw new FileDownloadException("Could not find the file!");
            }

            return s3is; // Return the InputStream directly
        } catch (NoSuchKeyException e) {
            throw new FileDownloadException("File not found in the bucket.");
        }
    }

    @Override
    public void delete(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    private boolean bucketIsEmpty() {
        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .maxKeys(1) // Only request 1 item to check if bucket is empty
                .build();

        ListObjectsV2Response result = s3Client.listObjectsV2(listObjectsRequest);
        return result.contents().isEmpty();
    }

    private String generateFileName(MultipartFile multipartFile) {
        String extension = "";
        String originalFileName = multipartFile.getOriginalFilename();

        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        return UUID.randomUUID() + extension;
    }
}
