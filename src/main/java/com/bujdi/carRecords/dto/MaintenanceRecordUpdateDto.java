package com.bujdi.carRecords.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class MaintenanceRecordUpdateDto {
    @NotEmpty(message = "The title is required")
    @Size(min = 3, max = 100, message = "The length of the title must be between 3 and 100 characters.")
    private String title;

    @NotNull(message = "The description is required")
    private Map<String, Object> description;

    @NotNull(message = "The date is required")
    @PastOrPresent(message = "The date cannot be in the future")
    private LocalDate date;
}
