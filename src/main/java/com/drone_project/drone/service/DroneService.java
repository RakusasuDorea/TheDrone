package com.drone_project.drone.service;

import com.drone_project.drone.exception.InsufficientBatteryException;
import com.drone_project.drone.exception.OverloadedDroneException;
import com.drone_project.drone.model.Drone;
import com.drone_project.drone.model.Medication;
import com.drone_project.drone.model.State;
import com.drone_project.drone.repository.DroneRepository;
import com.drone_project.drone.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DroneService {
    private static final Logger logger = LoggerFactory.getLogger(DroneService.class);

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    private final ConcurrentHashMap<String, Drone> deliveryQueue = new ConcurrentHashMap<>();

    public Drone registerDrone(Drone drone) {
        drone.setWeightLimitBasedOnModel();
        drone.setState(State.IDLE);
        return droneRepository.save(drone);
    }

    public Drone getDroneInfo(String serialNumber) {
        return droneRepository.findBySerialNumber(serialNumber);
    }

    public void loadMedication(String serialNumber, Medication medication) {
        Drone drone = droneRepository.findBySerialNumber(serialNumber);

        if (drone.getBatteryCapacity() < 25) {
            throw new InsufficientBatteryException("Cannot load medication. Battery level too low.");
        }

        int totalLoadedWeight = medicationRepository.findByDrone(drone)
                .stream()
                .mapToInt(Medication::getWeight)
                .sum();

        if (totalLoadedWeight + medication.getWeight() > drone.getWeightLimit()) {
            throw new OverloadedDroneException("Cannot load medication. Exceeds weight limit.");
        }

        if (drone.getState() != State.LOADING) {
            drone.setState(State.LOADING);
            droneRepository.save(drone);
        }

        medication.setDrone(drone);
        medicationRepository.save(medication);

        if (totalLoadedWeight + medication.getWeight() == drone.getWeightLimit()) {
            drone.setState(State.LOADED);
            droneRepository.save(drone);
        }
    }

    public List<Medication> getLoadedMedications(String serialNumber) {
        Drone drone = droneRepository.findBySerialNumber(serialNumber);
        return medicationRepository.findByDrone(drone);
    }

    public void completeDelivery(String serialNumber) {
        Drone drone = droneRepository.findBySerialNumber(serialNumber);

        logger.info("Current state of drone {}: {}", serialNumber, drone.getState());

        if (drone.getState() != State.DELIVERING) {
            drone.setState(State.DELIVERING);
            droneRepository.save(drone);
            logger.info("Drone {} state set to DELIVERING", serialNumber);
        }

        deliveryQueue.put(serialNumber, drone);
    }

    public List<Drone> getAvailableDrones() {
        return droneRepository.findByState(State.IDLE);
    }

    public int getBatteryLevel(String serialNumber) {
        Drone drone = droneRepository.findBySerialNumber(serialNumber);
        return drone.getBatteryCapacity();
    }

    @Scheduled(fixedDelay = 5000)
    public void processScheduledDeliveries() {
        for (String serialNumber : deliveryQueue.keySet()) {
            Drone drone = deliveryQueue.get(serialNumber);
            if (drone != null && drone.getState() == State.DELIVERING) {
                int currentBattery = drone.getBatteryCapacity();
                int reducedBattery = currentBattery - 10;
                drone.setBatteryCapacity(reducedBattery);

                drone.setState(State.DELIVERED);
                droneRepository.save(drone);
                logger.info("Drone {} state set to DELIVERED wit battery level {}", serialNumber, reducedBattery);

                deliveryQueue.remove(serialNumber);
            }
        }
    }
}
