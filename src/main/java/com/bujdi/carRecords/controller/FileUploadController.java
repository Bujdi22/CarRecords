package com.bujdi.carRecords.controller;

import com.bujdi.carRecords.mapping.APIResponse;
import com.bujdi.carRecords.dto.FileUploadDto;
import com.bujdi.carRecords.exception.FileDownloadException;
import com.bujdi.carRecords.exception.FileEmptyException;
import com.bujdi.carRecords.exception.FileUploadException;
import com.bujdi.carRecords.model.Media;
import com.bujdi.carRecords.service.FileService;
import com.bujdi.carRecords.service.MediaService;
import com.bujdi.carRecords.validation.annotation.ExistsInDatabase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/api/file")
@Validated
public class FileUploadController {
    private final FileService fileService;

    public FileUploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @Autowired
    private MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@Valid @ModelAttribute FileUploadDto dto) throws FileEmptyException, FileUploadException, IOException {

        if (!mediaService.validateAccess(dto.getModelType(), dto.getModelId())) {
            APIResponse apiResponse = APIResponse.builder()
                    .message("Invalid model")
                    .isSuccessful(false)
                    .statusCode(400)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        MultipartFile multipartFile = dto.getFile();
        if (multipartFile.isEmpty()){
            throw new FileEmptyException("File is empty. Cannot save an empty file");
        }
        boolean isValidFile = isValidFile(multipartFile);
        List<String> allowedFileExtensions = new ArrayList<>(Arrays.asList("pdf", "png", "jpg", "jpeg"));

        if (isValidFile && allowedFileExtensions.contains(FilenameUtils.getExtension(multipartFile.getOriginalFilename()))){
            String fileName = mediaService.upload(dto);
            APIResponse apiResponse = APIResponse.builder()
                    .message("file uploaded successfully. File unique name =>" + fileName)
                    .isSuccessful(true)
                    .statusCode(200)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            APIResponse apiResponse = APIResponse.builder()
                    .message("Invalid File. File extension or File name is not supported")
                    .isSuccessful(false)
                    .statusCode(400)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/download/{mediaId}")
    public ResponseEntity<?> downloadFile(@PathVariable("mediaId") @NotBlank @NotNull String mediaId) throws FileDownloadException, IOException {

        Optional<Media> media = mediaService.getMediaById(mediaId);

        if (media.isEmpty() || !mediaService.validateAccess(media.get())) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String fileName = media.get().getStoredPath();

        InputStream inputStream = fileService.downloadFile(fileName);

        if (inputStream != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            headers.setCacheControl("public, max-age=36000");
            String etag = "W/\"" + mediaId + "\"";
            headers.setETag(etag);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new InputStreamResource(inputStream));
        } else {
            APIResponse apiResponse = APIResponse.builder()
                    .message("File could not be downloaded")
                    .isSuccessful(false)
                    .statusCode(400)
                    .build();
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    private boolean isValidFile(MultipartFile multipartFile){
        log.info("Empty Status ==> {}", multipartFile.isEmpty());
        if (Objects.isNull(multipartFile.getOriginalFilename())){
            return false;
        }
        return !multipartFile.getOriginalFilename().trim().equals("");
    }
}
