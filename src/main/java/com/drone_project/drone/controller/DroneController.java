package com.drone_project.drone.controller;

import com.drone_project.drone.exception.InsufficientBatteryException;
import com.drone_project.drone.exception.OverloadedDroneException;
import com.drone_project.drone.model.Drone;
import com.drone_project.drone.model.Medication;
import com.drone_project.drone.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/drones")
public class DroneController {
    @Autowired
    private DroneService droneService;

    @PostMapping("/register")
    public ResponseEntity<Drone> registerDrone(@RequestBody Drone drone) {
        return ResponseEntity.ok(droneService.registerDrone(drone));
    }

    @GetMapping("/{serialNumber}")
    public ResponseEntity<Drone> getDroneInfo(@PathVariable String serialNumber) {
        Drone drone = droneService.getDroneInfo(serialNumber);
        if (drone == null) {
            return ResponseEntity.notFound().build(); // Return 404 if drone is not found
        }
        return ResponseEntity.ok(drone);
    }


    @PostMapping("/{serialNumber}/load")
    public ResponseEntity<String> loadMedication(@PathVariable String serialNumber, @Valid @RequestBody Medication medication) {
        try {
            droneService.loadMedication(serialNumber, medication);
            return ResponseEntity.ok("Medication loaded successfully");
        } catch (OverloadedDroneException | InsufficientBatteryException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        }
    }


    @GetMapping("/{serialNumber}/medications")
    public ResponseEntity<List<Medication>> getLoadedMedications(@PathVariable String serialNumber) {
        return ResponseEntity.ok(droneService.getLoadedMedications(serialNumber));
    }

    @PostMapping("/{serialNumber}/deliver")
    public ResponseEntity<String> completeDelivery(@PathVariable String serialNumber) {
        droneService.completeDelivery(serialNumber);
        return ResponseEntity.ok("Delivery completed successfully");
    }

    @GetMapping("/available")
    public ResponseEntity<List<Drone>> getAvailableDrones() {
        return ResponseEntity.ok(droneService.getAvailableDrones());
    }

    @GetMapping("/{serialNumber}/battery")
    public ResponseEntity<Map<String, Object>> getBatteryLevel(@PathVariable String serialNumber) {
        Map<String, Object> response = new HashMap<>();
        response.put("battery", droneService.getBatteryLevel(serialNumber));
        response.put("SerialNumber", serialNumber);
        return ResponseEntity.ok(response);
    }

}
