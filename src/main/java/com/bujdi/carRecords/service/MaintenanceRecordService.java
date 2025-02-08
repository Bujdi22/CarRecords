package com.bujdi.carRecords.service;

import com.bujdi.carRecords.dto.MaintenanceRecordDto;
import com.bujdi.carRecords.dto.MaintenanceRecordUpdateDto;
import com.bujdi.carRecords.model.MaintenanceRecord;
import com.bujdi.carRecords.model.Media;
import com.bujdi.carRecords.model.Vehicle;
import com.bujdi.carRecords.repository.MaintenanceRecordRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MaintenanceRecordService {
    @Autowired
    MaintenanceRecordRepository repo;

    @Autowired
    MediaService mediaService;

    @PersistenceContext
    private EntityManager entityManager;


    public List<MaintenanceRecord> getRecordsForVehicle(UUID vehicleId) {
        return repo.findByVehicleIdAndDeletedAtIsNull(vehicleId);
    }

    public int getRecordCountForVehicle(UUID vehicleId) {
        return repo.countByVehicleIdAndDeletedAtIsNull(vehicleId);
    }

    public MaintenanceRecord addRecord(MaintenanceRecordDto dto) {
        LocalDateTime now = LocalDateTime.now();

        MaintenanceRecord record = new MaintenanceRecord();

        Vehicle vehicleRef = entityManager.getReference(Vehicle.class, dto.getVehicleId());
        record.setVehicle(vehicleRef);
        record.setTitle(dto.getTitle());
        record.setDescription(dto.getDescription());
        record.setDate(dto.getDate());
        record.setOdometer(dto.getOdometer());

        record.setCreatedAt(now);
        record.setUpdatedAt(now);

        return repo.save(record);
    }

    public Optional<MaintenanceRecord> getRecordById(UUID recordId) {
        return repo.findByIdAndDeletedAtIsNull(recordId);
    }

    public MaintenanceRecord updateRecord(MaintenanceRecord record, MaintenanceRecordUpdateDto dto) {
        record.setTitle(dto.getTitle());
        record.setDescription(dto.getDescription());
        record.setDate(dto.getDate());
        record.setOdometer(dto.getOdometer());
        record.setUpdatedAt(LocalDateTime.now());


        if (dto.getDeleteFiles() != null && !dto.getDeleteFiles().isEmpty()) {
            for (String fileId : dto.getDeleteFiles()) {
                Optional<Media> optionalMedia = mediaService.getMediaById(fileId);
                if (optionalMedia.isPresent()) {
                    Media media = optionalMedia.get();
                    if (mediaService.validateAccess(media)) {
                        mediaService.deleteMedia(media);
                    }
                }
            }
        }

        return repo.save(record);
    }

    public void deleteRecord(MaintenanceRecord maintenanceRecord) {
        maintenanceRecord.setDeletedAt(LocalDateTime.now());
        repo.save(maintenanceRecord);
    }
}
