package com.bujdi.carRecords.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class GroupInviteResponseDto {
    @NotEmpty(message = "Your invite token was not attached")
    private String token;
}
