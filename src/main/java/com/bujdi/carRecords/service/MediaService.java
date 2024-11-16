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
import java.util.List;
import java.util.Optional;
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

        AccessValidatable entity = entityManager.find(modelClass, id);

        if (entity == null) {
            return false;
        }

        return entity.hasUserAccess(userService.getAuthUser().getId());
    }

    public Optional<Media> getMediaById(String mediaId) {
        return mediaRepository.findById(UUID.fromString(mediaId));
    }

    public Boolean validateAccess(Media media) {
        String typeString = media.getModelType();
        int id = media.getModelId();

        return this.validateAccess(typeString, id);
    }

    public String upload(FileUploadDto dto) throws FileUploadException, IOException {
        MultipartFile multipartFile = dto.getFile();
        String fileName = fileService.uploadFile(multipartFile);
        String uuid = fileName.substring(0, fileName.lastIndexOf("."));
        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);

        Media media = new Media();
        media.setId(UUID.fromString(uuid));
        media.setFileType(fileType);
        media.setModelType(dto.getModelType());
        media.setModelId(dto.getModelId());
        mediaRepository.save(media);

        return fileName;
    }

    public List<Media> getMediaForModel(Class<? extends AccessValidatable> model, int id)
    {
        String mediaTypeString = getTypeStringFromClass(model);

        return mediaRepository.findMediaByModelIdAndModelType(id, mediaTypeString);
    }

    private Class<? extends AccessValidatable> getClassFromTypeString(String modelType) {
        if (modelType.equals("maintenance_record")) {
            return MaintenanceRecord.class;
        }
        throw new IllegalArgumentException("Invalid model type string for getClassFromTypeString");
    }

    private String getTypeStringFromClass(Class<? extends AccessValidatable> clazz) {
        if (clazz == MaintenanceRecord.class) {
            return "maintenance_record";
        }
        throw new IllegalArgumentException("Invalid class type for getTypeStringFromClass");
    }
}
