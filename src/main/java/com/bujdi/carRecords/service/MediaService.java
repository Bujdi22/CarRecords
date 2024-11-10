package com.bujdi.carRecords.service;

import com.bujdi.carRecords.dto.FileUploadDto;
import com.bujdi.carRecords.exception.FileUploadException;
import com.bujdi.carRecords.model.MaintenanceRecord;
import com.bujdi.carRecords.model.Media;
import com.bujdi.carRecords.repository.MediaRepository;
import com.bujdi.carRecords.validation.AccessValidatable;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class MediaService {

    private final FileService fileService;
    public MediaService(FileService fileService) {
        this.fileService = fileService;
    }
    @Autowired
    UserService userService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private MediaRepository mediaRepository;

    public Boolean validateAccess(String modelType, int id) {
        Class<? extends AccessValidatable> modelClass = getClassFromTypeString(modelType);
        if (modelClass == null) {
            throw new IllegalArgumentException("Invalid model type");
        }

        AccessValidatable entity = entityManager.find(modelClass, id);

        if (entity == null) {
            return false;
        }

        return entity.hasUserAccess(userService.getAuthUser().getId());
    }

    public String upload(FileUploadDto dto) throws FileUploadException, IOException {
        MultipartFile multipartFile = dto.getFile();
        String fileName = fileService.uploadFile(multipartFile);
        String uuid = fileName.substring(0, fileName.lastIndexOf("."));

        Media media = new Media();
        media.setId(UUID.fromString(uuid));
        media.setFilename(fileName);
        media.setModelType(dto.getModelType());
        media.setModelId(dto.getModelId());
        mediaRepository.save(media);

        return fileName;
    }

    private Class<? extends AccessValidatable> getClassFromTypeString(String modelType) {
        if (modelType.equals("maintenance_record")) {
            return MaintenanceRecord.class;
        }
        return null;
    }
}
