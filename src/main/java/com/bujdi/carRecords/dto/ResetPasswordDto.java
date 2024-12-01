package com.bujdi.carRecords.dto;

import com.bujdi.carRecords.validation.annotation.ValidPassword;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordDto {
    @ValidPassword @NotNull(message = "The password is required")
    private String password;

    @NotEmpty(message = "Your reset password token was not attached")
    private String token;
}
