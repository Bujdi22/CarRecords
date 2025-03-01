package com.bujdi.carRecords.mapping;

import com.bujdi.carRecords.model.MaintenanceRecord;
import com.bujdi.carRecords.model.Media;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class MaintenanceRecordResponse {
    private List<Media> media;
    private UUID id;
    private String title;
    private Integer odometer;
    private Map<String, Object> description;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID vehicleId;

    public MaintenanceRecordResponse(MaintenanceRecord record, List<Media> media) {
        this.id = record.getId();
        this.title = record.getTitle();
        this.description = record.getDescription();
        this.date = record.getDate();
        this.createdAt = record.getCreatedAt();
        this.updatedAt = record.getUpdatedAt();
        this.media = media;
        this.vehicleId = record.getVehicle().getId();
        this.odometer = record.getOdometer();
    }

    public MaintenanceRecordResponse(MaintenanceRecord record) {
        this.id = record.getId();
        this.title = record.getTitle();
        this.description = record.getDescription();
        this.date = record.getDate();
        this.createdAt = record.getCreatedAt();
        this.updatedAt = record.getUpdatedAt();
        this.vehicleId = record.getVehicle().getId();
        this.odometer = record.getOdometer();
    }
}
