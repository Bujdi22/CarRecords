package com.bujdi.carRecords.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Media {
    @Id
    private UUID id;
    private String modelType;
    private UUID modelId;
    private String fileType;
    private LocalDateTime createdAt;

    public String getStoredPath() {
        return this.id.toString() + "." + this.fileType;
    }
}
