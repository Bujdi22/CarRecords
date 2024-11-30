package com.bujdi.carRecords.dto;

import com.bujdi.carRecords.model.Vehicle;
import com.bujdi.carRecords.validation.annotation.ExistsInDatabase;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class MaintenanceRecordDto extends MaintenanceRecordUpdateDto{

    @NotNull
    @ExistsInDatabase(entity = Vehicle.class, field = "id", message = "Vehicle does not exist", belongsToUser = true)
    private UUID vehicleId;
}
