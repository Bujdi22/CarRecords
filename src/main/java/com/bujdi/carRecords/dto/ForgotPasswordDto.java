package com.bujdi.carRecords.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ForgotPasswordDto {

    @NotEmpty(message = "The email is required")
    @Size(min = 3, max = 100, message = "The length of the name must be between 3 and 100 characters.")
    private String username;
}
