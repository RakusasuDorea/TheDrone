package com.drone_project.drone.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicationResponseDTO {

    private Long id;
    private String name;
    private int weight;
    private String code;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
