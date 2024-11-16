package com.bujdi.carRecords.mapping;

import com.bujdi.carRecords.model.MaintenanceRecord;
import com.bujdi.carRecords.model.Media;
import com.bujdi.carRecords.model.Vehicle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class MaintenanceRecordResponse {
    private List<Media> media;
    private int id;
    private String title;
    private Map<String, Object> description;
    private LocalDate date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MaintenanceRecordResponse(MaintenanceRecord record, List<Media> media) {
        this.id = record.getId();
        this.title = record.getTitle();
        this.description = record.getDescription();
        this.date = record.getDate();
        this.createdAt = record.getCreatedAt();
        this.updatedAt = record.getUpdatedAt();
        this.media = media;
    }
}
