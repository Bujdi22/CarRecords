package com.bujdi.carRecords.dto;

import com.bujdi.carRecords.model.Group;
import com.bujdi.carRecords.validation.annotation.ExistsInDatabase;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class GroupInviteDto {
    @NotNull
    @ExistsInDatabase(entity = Group.class, field = "id", message = "Group does not exist")
    private UUID groupId;

    @NotEmpty(message = "The email is required")
    @Size(min = 3, max = 100, message = "The length of the name must be between 3 and 100 characters.")
    @Email
    private String username;

}
