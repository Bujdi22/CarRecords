package com.bujdi.carRecords.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class GroupDto {

    @NotEmpty(message = "The name is required")
    @Size(min = 3, max = 100, message = "The length of the name must be between 3 and 100 characters.")
    private String name;

    @NotNull(message = "The readonly toggle is required")
    private Boolean readonly;
}
