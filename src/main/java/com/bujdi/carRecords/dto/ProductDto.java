package com.bujdi.carRecords.dto;

import com.bujdi.carRecords.model.Vendor;
import com.bujdi.carRecords.validation.annotation.ExistsInDatabase;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class ProductDto {

    @NotEmpty(message = "The name is required")
    @Size(min = 3, max = 100, message = "The length of the name must be between 3 and 100 characters.")
    private String name;


    @NotEmpty(message = "The description is required")
    @Size(min = 3, max = 255, message = "The length of the description must be between 3 and 255 characters.")
    private String description;

    @Max(999999)
    private int price;

    @Max(999999)
    private int quantity;

    private boolean isAvailable;

    @Min(value = 1, message = "The vendor is required")
    @ExistsInDatabase(entity = Vendor.class, field = "id", message = "Vendor does not exist")
    private int vendorId;
}
