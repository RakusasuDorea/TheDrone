package com.drone_project.drone.controller;

import com.drone_project.drone.dto.DroneRequestDTO;
import com.drone_project.drone.dto.DroneResponseDTO;
import com.drone_project.drone.dto.MedicationRequestDTO;
import com.drone_project.drone.dto.MedicationResponseDTO;
import com.drone_project.drone.service.DroneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/drones")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @PostMapping("/register")
    public ResponseEntity<DroneResponseDTO> registerDrone(@Valid @RequestBody DroneRequestDTO request) {
        return ResponseEntity.ok(droneService.registerDrone(request));
    }

    @GetMapping("/{serialNumber}")
    public ResponseEntity<DroneResponseDTO> getDroneInfo(@PathVariable String serialNumber) {
        return ResponseEntity.ok(droneService.getDroneInfo(serialNumber));
    }

    @PostMapping("/{serialNumber}/load")
    public ResponseEntity<String> loadMedication(@PathVariable String serialNumber,
                                                 @Valid @RequestBody MedicationRequestDTO request) {
        droneService.loadMedication(serialNumber, request);
        return ResponseEntity.ok("Medication loaded successfully");
    }

    @GetMapping("/{serialNumber}/medications")
    public ResponseEntity<List<MedicationResponseDTO>> getLoadedMedications(@PathVariable String serialNumber) {
        return ResponseEntity.ok(droneService.getLoadedMedications(serialNumber));
    }

    @PostMapping("/{serialNumber}/deliver")
    public ResponseEntity<String> completeDelivery(@PathVariable String serialNumber) {
        droneService.completeDelivery(serialNumber);
        return ResponseEntity.ok("Delivery completed successfully");
    }

    @GetMapping("/available")
    public ResponseEntity<List<DroneResponseDTO>> getAvailableDrones() {
        return ResponseEntity.ok(droneService.getAvailableDrones());
    }

    @PostMapping("/{serialNumber}/return")
    public ResponseEntity<String> returnDrone(@PathVariable String serialNumber) {
        droneService.setAvailableDrones(serialNumber);
        return ResponseEntity.ok("Drone returned successfully");
    }

    @GetMapping("/{serialNumber}/battery")
    public ResponseEntity<Map<String, Object>> getBatteryLevel(@PathVariable String serialNumber) {
        return ResponseEntity.ok(Map.of(
                "serialNumber", serialNumber,
                "battery", droneService.getBatteryLevel(serialNumber)
        ));
    }
}
