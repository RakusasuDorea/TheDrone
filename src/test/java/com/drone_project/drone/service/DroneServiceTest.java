package com.drone_project.drone.service;

import com.drone_project.drone.dto.DroneRequestDTO;
import com.drone_project.drone.dto.DroneResponseDTO;
import com.drone_project.drone.dto.MedicationRequestDTO;
import com.drone_project.drone.exception.DroneNotFoundException;
import com.drone_project.drone.exception.InsufficientBatteryException;
import com.drone_project.drone.exception.OverloadedDroneException;
import com.drone_project.drone.mapper.DroneMapper;
import com.drone_project.drone.mapper.MedicationMapper;
import com.drone_project.drone.model.Drone;
import com.drone_project.drone.model.Medication;
import com.drone_project.drone.model.Model;
import com.drone_project.drone.model.State;
import com.drone_project.drone.repository.DroneRepository;
import com.drone_project.drone.repository.MedicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private MedicationRepository medicationRepository;

    @Mock
    private DroneMapper droneMapper;

    @Mock
    private MedicationMapper medicationMapper;

    @InjectMocks
    private DroneService droneService;

    // ── registerDrone ────────────────────────────────────────────────────────

    @Test
    void registerDrone_setsStateToIdleAndReturnsDTO() {
        DroneRequestDTO request = new DroneRequestDTO();
        Drone drone = new Drone();
        DroneResponseDTO responseDTO = new DroneResponseDTO();

        when(droneMapper.toEntity(request)).thenReturn(drone);
        when(droneRepository.save(drone)).thenReturn(drone);
        when(droneMapper.toResponseDTO(drone)).thenReturn(responseDTO);

        DroneResponseDTO result = droneService.registerDrone(request);

        assertEquals(State.IDLE, drone.getState());
        assertEquals(responseDTO, result);
        verify(droneRepository).save(drone);
    }

    // ── getDroneInfo ─────────────────────────────────────────────────────────

    @Test
    void getDroneInfo_returnsDTOWhenFound() {
        Drone drone = new Drone();
        DroneResponseDTO responseDTO = new DroneResponseDTO();

        when(droneRepository.findBySerialNumber("DRONE001")).thenReturn(drone);
        when(droneMapper.toResponseDTO(drone)).thenReturn(responseDTO);

        DroneResponseDTO result = droneService.getDroneInfo("DRONE001");

        assertEquals(responseDTO, result);
    }

    @Test
    void getDroneInfo_throwsWhenNotFound() {
        when(droneRepository.findBySerialNumber("UNKNOWN")).thenReturn(null);

        assertThrows(DroneNotFoundException.class, () -> droneService.getDroneInfo("UNKNOWN"));
    }

    // ── loadMedication ───────────────────────────────────────────────────────

    @Test
    void loadMedication_throwsWhenBatteryTooLow() {
        Drone drone = droneWithBattery(20, Model.Lightweight);
        when(droneRepository.findBySerialNumber("DRONE001")).thenReturn(drone);

        MedicationRequestDTO request = medicationRequest(100);

        assertThrows(InsufficientBatteryException.class,
                () -> droneService.loadMedication("DRONE001", request));
    }

    @Test
    void loadMedication_throwsWhenExceedsWeightLimit() {
        Drone drone = droneWithBattery(100, Model.Lightweight); // weight limit = 250
        when(droneRepository.findBySerialNumber("DRONE001")).thenReturn(drone);
        when(medicationRepository.findByDrone(drone)).thenReturn(List.of(medicationWithWeight(200)));

        MedicationRequestDTO request = medicationRequest(100); // 200 + 100 > 250

        assertThrows(OverloadedDroneException.class,
                () -> droneService.loadMedication("DRONE001", request));
    }

    @Test
    void loadMedication_setsStateToLoadingAndSaves() {
        Drone drone = droneWithBattery(100, Model.Heavyweight); // weight limit = 1000
        drone.setState(State.IDLE);
        when(droneRepository.findBySerialNumber("DRONE001")).thenReturn(drone);
        when(medicationRepository.findByDrone(drone)).thenReturn(List.of());
        when(medicationMapper.toEntity(any())).thenReturn(new Medication());

        MedicationRequestDTO request = medicationRequest(100);
        droneService.loadMedication("DRONE001", request);

        assertEquals(State.LOADING, drone.getState());
        verify(droneRepository, atLeastOnce()).save(drone);
        verify(medicationRepository).save(any(Medication.class));
    }

    @Test
    void loadMedication_setsStateToLoadedWhenFull() {
        Drone drone = droneWithBattery(100, Model.Lightweight); // weight limit = 250
        drone.setState(State.LOADING);
        when(droneRepository.findBySerialNumber("DRONE001")).thenReturn(drone);
        when(medicationRepository.findByDrone(drone)).thenReturn(List.of(medicationWithWeight(150)));
        when(medicationMapper.toEntity(any())).thenReturn(new Medication());

        MedicationRequestDTO request = medicationRequest(100); // 150 + 100 = 250 (exactly full)
        droneService.loadMedication("DRONE001", request);

        assertEquals(State.LOADED, drone.getState());
    }

    @Test
    void loadMedication_throwsWhenDroneNotFound() {
        when(droneRepository.findBySerialNumber("UNKNOWN")).thenReturn(null);

        assertThrows(DroneNotFoundException.class,
                () -> droneService.loadMedication("UNKNOWN", medicationRequest(100)));
    }

    // ── getBatteryLevel ──────────────────────────────────────────────────────

    @Test
    void getBatteryLevel_returnsCorrectLevel() {
        Drone drone = droneWithBattery(85, Model.Lightweight);
        when(droneRepository.findBySerialNumber("DRONE001")).thenReturn(drone);

        assertEquals(85, droneService.getBatteryLevel("DRONE001"));
    }

    @Test
    void getBatteryLevel_throwsWhenNotFound() {
        when(droneRepository.findBySerialNumber("UNKNOWN")).thenReturn(null);

        assertThrows(DroneNotFoundException.class,
                () -> droneService.getBatteryLevel("UNKNOWN"));
    }

    // ── getAvailableDrones ───────────────────────────────────────────────────

    @Test
    void getAvailableDrones_returnsOnlyIdleDrones() {
        Drone drone = new Drone();
        DroneResponseDTO responseDTO = new DroneResponseDTO();

        when(droneRepository.findByState(State.IDLE)).thenReturn(List.of(drone));
        when(droneMapper.toResponseDTO(drone)).thenReturn(responseDTO);

        List<DroneResponseDTO> result = droneService.getAvailableDrones();

        assertEquals(1, result.size());
        assertEquals(responseDTO, result.get(0));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Drone droneWithBattery(int battery, Model model) {
        Drone drone = new Drone();
        drone.setBatteryCapacity(battery);
        drone.setModel(model);
        drone.setState(State.IDLE);
        return drone;
    }

    private Medication medicationWithWeight(int weight) {
        Medication medication = new Medication();
        medication.setWeight(weight);
        return medication;
    }

    private MedicationRequestDTO medicationRequest(int weight) {
        MedicationRequestDTO dto = new MedicationRequestDTO();
        dto.setWeight(weight);
        dto.setName("Painkiller");
        dto.setCode("PAIN_K01");
        return dto;
    }
}
