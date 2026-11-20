package com.drone_project.drone.mapper;

import com.drone_project.drone.dto.DroneRequestDTO;
import com.drone_project.drone.dto.DroneResponseDTO;
import com.drone_project.drone.model.Drone;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = MedicationMapper.class)
public interface DroneMapper {

    Drone toEntity(DroneRequestDTO dto);

    @Mapping(source = "weightLimit", target = "weightLimit")
    DroneResponseDTO toResponseDTO(Drone drone);
}
