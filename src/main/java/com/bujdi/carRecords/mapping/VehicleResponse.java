package com.bujdi.carRecords.mapping;

import com.bujdi.carRecords.model.Vehicle;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class VehicleResponse {
    private UUID id;
    private String displayName;
    private String make;
    private String model;
    private String registration;
    private int year;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int recordCount;

    public VehicleResponse(Vehicle vehicle, int recordCount)
    {
        this.id = vehicle.getId();
        this.displayName = vehicle.getDisplayName();
        this.make = vehicle.getMake();
        this.model = vehicle.getModel();
        this.registration = vehicle.getRegistration();
        this.year = vehicle.getYear();
        this.createdAt = vehicle.getCreatedAt();
        this.updatedAt = vehicle.getUpdatedAt();
        this.recordCount = recordCount;
    }
}
