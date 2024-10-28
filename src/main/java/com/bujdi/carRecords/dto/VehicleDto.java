package com.bujdi.carRecords.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VehicleDto {

    @NotEmpty(message = "The display name required")
    @Size(min = 3, max = 100, message = "The length of the name must be between 3 and 100 characters.")
    private String displayName;

    @NotEmpty(message = "The make name required")
    @Size(min = 2, max = 200, message = "The length of the name must be between 2 and 200 characters.")
    private String make;

    @NotEmpty(message = "The make name required")
    @Size(min = 2, max = 200, message = "The length of the name must be between 2 and 200 characters.")
    private String model;

    @Min(value = 1900, message = "The year must not be earlier than 1900")
    @Max(value = 2100, message = "The year cannot be after 2100")
    private int year;

}
