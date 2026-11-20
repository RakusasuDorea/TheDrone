package com.drone_project.drone.exception;

public class DroneNotFoundException extends RuntimeException {
    public DroneNotFoundException(String serialNumber) {
        super("Drone not found with serial number: " + serialNumber);
    }
}
