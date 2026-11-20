package com.drone_project.drone.controller;

import com.drone_project.drone.dto.DroneRequestDTO;
import com.drone_project.drone.dto.DroneResponseDTO;
import com.drone_project.drone.dto.MedicationRequestDTO;
import com.drone_project.drone.dto.MedicationResponseDTO;
import com.drone_project.drone.service.DroneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/drones")
@RequiredArgsConstructor
@Tag(name = "Drone Fleet", description = "Manage drones and medication deliveries")
public class DroneController {

    private final DroneService droneService;

    @Operation(summary = "Register a new drone")
    @PostMapping("/register")
    public ResponseEntity<DroneResponseDTO> registerDrone(@Valid @RequestBody DroneRequestDTO request) {
        return ResponseEntity.ok(droneService.registerDrone(request));
    }

    @Operation(summary = "Get drone information by serial number")
    @GetMapping("/{serialNumber}")
    public ResponseEntity<DroneResponseDTO> getDroneInfo(@PathVariable String serialNumber) {
        return ResponseEntity.ok(droneService.getDroneInfo(serialNumber));
    }

    @Operation(summary = "Load a medication onto a drone")
    @PostMapping("/{serialNumber}/load")
    public ResponseEntity<String> loadMedication(@PathVariable String serialNumber,
                                                 @Valid @RequestBody MedicationRequestDTO request) {
        droneService.loadMedication(serialNumber, request);
        return ResponseEntity.ok("Medication loaded successfully");
    }

    @Operation(summary = "Get all medications loaded on a drone")
    @GetMapping("/{serialNumber}/medications")
    public ResponseEntity<List<MedicationResponseDTO>> getLoadedMedications(@PathVariable String serialNumber) {
        return ResponseEntity.ok(droneService.getLoadedMedications(serialNumber));
    }

    @Operation(summary = "Dispatch a drone for delivery")
    @PostMapping("/{serialNumber}/deliver")
    public ResponseEntity<String> completeDelivery(@PathVariable String serialNumber) {
        droneService.completeDelivery(serialNumber);
        return ResponseEntity.ok("Delivery completed successfully");
    }

    @Operation(summary = "Get all available (IDLE) drones")
    @GetMapping("/available")
    public ResponseEntity<List<DroneResponseDTO>> getAvailableDrones() {
        return ResponseEntity.ok(droneService.getAvailableDrones());
    }

    @Operation(summary = "Return a drone to IDLE state")
    @PostMapping("/{serialNumber}/return")
    public ResponseEntity<String> returnDrone(@PathVariable String serialNumber) {
        droneService.setAvailableDrones(serialNumber);
        return ResponseEntity.ok("Drone returned successfully");
    }

    @Operation(summary = "Get battery level of a drone")
    @GetMapping("/{serialNumber}/battery")
    public ResponseEntity<Map<String, Object>> getBatteryLevel(@PathVariable String serialNumber) {
        return ResponseEntity.ok(Map.of(
                "serialNumber", serialNumber,
                "battery", droneService.getBatteryLevel(serialNumber)
        ));
    }
}
