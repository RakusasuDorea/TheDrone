package com.drone_project.drone.dto;

import com.drone_project.drone.model.Model;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DroneRequestDTO {

    @NotBlank(message = "Serial number is required.")
    private String serialNumber;

    @NotNull(message = "Model is required.")
    private Model model;

    @Min(value = 0, message = "Battery capacity cannot be negative.")
    @Max(value = 100, message = "Battery capacity cannot exceed 100.")
    private int batteryCapacity;
}
