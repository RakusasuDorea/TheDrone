package com.drone_project.drone.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class MedicationRequestDTO {

    @NotBlank(message = "Name is required.")
    @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "Name can only contain letters, numbers, dashes, and underscores.")
    private String name;

    @NotNull(message = "Weight is required.")
    @Min(value = 1, message = "Weight must be greater than 0.")
    private int weight;

    @NotBlank(message = "Code is required.")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Code can only contain uppercase letters, numbers, and underscores.")
    private String code;

    private String image;
}
