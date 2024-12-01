package com.bujdi.carRecords.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VerifyEmailDTO {
    @NotEmpty(message = "Your verification token was not attached")
    private String token;
}
