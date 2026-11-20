package com.drone_project.drone.dto;

import com.drone_project.drone.model.Model;
import com.drone_project.drone.model.State;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DroneResponseDTO {

    private Long id;
    private String serialNumber;
    private Model model;
    private int weightLimit;
    private int batteryCapacity;
    private State state;
    private List<MedicationResponseDTO> medications;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
