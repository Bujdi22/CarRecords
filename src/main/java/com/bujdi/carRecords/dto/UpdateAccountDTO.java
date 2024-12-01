package com.bujdi.carRecords.dto;

import com.bujdi.carRecords.validation.annotation.ConditionalFieldRequired;
import com.bujdi.carRecords.validation.annotation.ValidPassword;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@ConditionalFieldRequired(fieldToCheck = "password", fieldToValidate = "currentPassword", message = "The current password is required when creating a new password")
public class UpdateAccountDTO {
    @NotEmpty(message = "The display name is required")
    @Size(min = 3, max = 100, message = "The length of the display name must be between 3 and 100 characters.")
    private String displayName;

    @ValidPassword
    private String password;

    private String currentPassword;
}
