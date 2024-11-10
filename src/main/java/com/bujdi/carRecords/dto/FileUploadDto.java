package com.bujdi.carRecords.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileUploadDto {
    @NotNull(message = "File is required")
    private MultipartFile file;

    @NotNull(message = "Model type is required")
    @Pattern(regexp = "maintenance_record|other_types_placeholder", message = "Invalid model type")
    private String modelType;

    @NotNull(message = "Model ID is required")
    private int modelId;
}
