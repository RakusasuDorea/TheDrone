package com.drone_project.drone.service;

import com.drone_project.drone.dto.DroneRequestDTO;
import com.drone_project.drone.dto.DroneResponseDTO;
import com.drone_project.drone.dto.MedicationRequestDTO;
import com.drone_project.drone.dto.MedicationResponseDTO;
import com.drone_project.drone.exception.DroneNotFoundException;
import com.drone_project.drone.exception.InvalidDroneStateException;
import com.drone_project.drone.exception.InsufficientBatteryException;
import com.drone_project.drone.exception.MedicationsLoadedException;
import com.drone_project.drone.exception.NoMedicationsLoadedException;
import com.drone_project.drone.exception.OverloadedDroneException;
import com.drone_project.drone.mapper.DroneMapper;
import com.drone_project.drone.mapper.MedicationMapper;
import com.drone_project.drone.model.Drone;
import com.drone_project.drone.model.Medication;
import com.drone_project.drone.model.State;
import com.drone_project.drone.repository.DroneRepository;
import com.drone_project.drone.repository.MedicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DroneService {

    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final DroneMapper droneMapper;
    private final MedicationMapper medicationMapper;

    private final Map<String, Drone> deliveryQueue = new HashMap<>();
    private final Map<String, Drone> returnQueue = new HashMap<>();

    public DroneResponseDTO registerDrone(DroneRequestDTO request) {
        Drone drone = droneMapper.toEntity(request);
        drone.setState(State.IDLE);
        return droneMapper.toResponseDTO(droneRepository.save(drone));
    }

    public DroneResponseDTO getDroneInfo(String serialNumber) {
        return droneMapper.toResponseDTO(findDroneOrThrow(serialNumber));
    }

    public void loadMedication(String serialNumber, MedicationRequestDTO request) {
        Drone drone = findDroneOrThrow(serialNumber);

        if (drone.getState() == State.DELIVERING || drone.getState() == State.DELIVERED || drone.getState() == State.RETURNING) {
            throw new InvalidDroneStateException("Cannot load medication. Drone is currently in " + drone.getState() + " state.");
        }

        if (drone.getBatteryCapacity() < 25) {
            throw new InsufficientBatteryException("Cannot load medication. Battery level too low.");
        }

        int totalLoadedWeight = medicationRepository.findByDrone(drone)
                .stream()
                .mapToInt(Medication::getWeight)
                .sum();

        if (totalLoadedWeight + request.getWeight() > drone.getWeightLimit()) {
            throw new OverloadedDroneException("Cannot load medication. Exceeds weight limit.");
        }

        if (drone.getState() != State.LOADING) {
            drone.setState(State.LOADING);
            droneRepository.save(drone);
        }

        Medication medication = medicationMapper.toEntity(request);
        medication.setDrone(drone);
        medicationRepository.save(medication);

        if (totalLoadedWeight + request.getWeight() == drone.getWeightLimit()) {
            drone.setState(State.LOADED);
            droneRepository.save(drone);
        }
    }

    public List<MedicationResponseDTO> getLoadedMedications(String serialNumber) {
        Drone drone = findDroneOrThrow(serialNumber);
        return medicationRepository.findByDrone(drone)
                .stream()
                .map(medicationMapper::toResponseDTO)
                .toList();
    }

    public void completeDelivery(String serialNumber) {
        Drone drone = findDroneOrThrow(serialNumber);
        List<Medication> medications = medicationRepository.findByDrone(drone);

        if (medications == null || medications.isEmpty()) {
            throw new NoMedicationsLoadedException("Drone has no medications loaded. Cannot set to DELIVERING state.");
        }

        if (drone.getState() != State.DELIVERING) {
            drone.setState(State.DELIVERING);
            droneRepository.save(drone);
            log.info("Drone {} state set to DELIVERING", serialNumber);
        }

        deliveryQueue.put(serialNumber, drone);
    }

    public void setAvailableDrones(String serialNumber) {
        Drone drone = findDroneOrThrow(serialNumber);

        if (drone.getState() != State.DELIVERED) {
            throw new InvalidDroneStateException("Cannot return drone. Drone must be in DELIVERED state before returning. Current state: " + drone.getState() + ".");
        }

        List<Medication> medications = medicationRepository.findByDrone(drone);
        if (medications != null && !medications.isEmpty()) {
            throw new MedicationsLoadedException("Drone has medication loaded and cannot be returned.");
        }

        if (drone.getState() != State.RETURNING) {
            drone.setState(State.RETURNING);
            droneRepository.save(drone);
            log.info("Drone {} state set to RETURNING", serialNumber);
        }

        returnQueue.put(serialNumber, drone);
    }

    public List<DroneResponseDTO> getAvailableDrones() {
        return droneRepository.findByState(State.IDLE)
                .stream()
                .map(droneMapper::toResponseDTO)
                .toList();
    }

    public int getBatteryLevel(String serialNumber) {
        return findDroneOrThrow(serialNumber).getBatteryCapacity();
    }

    public void unloadMedications(String serialNumber) {
        Drone drone = findDroneOrThrow(serialNumber);
        List<Medication> medications = medicationRepository.findByDrone(drone);
        medicationRepository.deleteAll(medications);
        log.info("All medications unloaded from drone {}", serialNumber);
    }

    @Scheduled(fixedDelay = 10000)
    public void processScheduledDeliveries() {
        for (String serialNumber : deliveryQueue.keySet()) {
            Drone drone = deliveryQueue.get(serialNumber);
            if (drone != null && drone.getState() == State.DELIVERING) {
                int reducedBattery = drone.getBatteryCapacity() - 10;
                drone.setBatteryCapacity(reducedBattery);
                drone.setState(State.DELIVERED);
                droneRepository.save(drone);
                log.info("Drone {} state set to DELIVERED with battery level {}", serialNumber, reducedBattery);
                unloadMedications(serialNumber);
                deliveryQueue.remove(serialNumber);
            }
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void processScheduledReturning() {
        for (String serialNumber : returnQueue.keySet()) {
            Drone drone = returnQueue.get(serialNumber);
            if (drone != null && drone.getState() == State.RETURNING) {
                drone.setState(State.IDLE);
                droneRepository.save(drone);
                returnQueue.remove(serialNumber);
            }
        }
    }

    private Drone findDroneOrThrow(String serialNumber) {
        Drone drone = droneRepository.findBySerialNumber(serialNumber);
        if (drone == null) {
            throw new DroneNotFoundException(serialNumber);
        }
        return drone;
    }
}
