package com.bujdi.carRecords.dto;

import com.bujdi.carRecords.model.User;
import com.bujdi.carRecords.validation.annotation.UniqueInDatabase;
import com.bujdi.carRecords.validation.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {

        @NotEmpty(message = "The email is required")
        @Size(min = 3, max = 100, message = "The length of the name must be between 3 and 100 characters.")
        @UniqueInDatabase(message = "The username is already taken.", entity = User.class, field = "username")
        @Email
        private String username;

        @NotEmpty(message = "The display name is required")
        @Size(min = 3, max = 100, message = "The length of the display name must be between 3 and 100 characters.")
        private String displayName;

        @ValidPassword
        private String password;
}
