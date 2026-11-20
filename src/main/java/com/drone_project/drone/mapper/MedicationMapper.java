package com.drone_project.drone.mapper;

import com.drone_project.drone.dto.MedicationRequestDTO;
import com.drone_project.drone.dto.MedicationResponseDTO;
import com.drone_project.drone.model.Medication;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicationMapper {

    Medication toEntity(MedicationRequestDTO dto);

    MedicationResponseDTO toResponseDTO(Medication medication);
}
