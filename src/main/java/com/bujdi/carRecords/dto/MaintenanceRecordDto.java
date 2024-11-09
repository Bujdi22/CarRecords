package com.bujdi.carRecords.dto;

import com.bujdi.carRecords.model.Vehicle;
import com.bujdi.carRecords.validation.annotation.ExistsInDatabase;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MaintenanceRecordDto extends MaintenanceRecordUpdateDto{

    @Min(value = 1, message = "The vehicle is required")
    @ExistsInDatabase(entity = Vehicle.class, field = "id", message = "Vehicle does not exist", belongsToUser = true)
    private int vehicleId;
}
