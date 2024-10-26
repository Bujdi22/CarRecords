package com.bujdi.carRecords.dto;

import com.bujdi.carRecords.validation.annotation.ValidPassword;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ResetPasswordDto {
    @ValidPassword
    private String password;

    @NotEmpty(message = "Your reset password token was not attached")
    private String token;
}
