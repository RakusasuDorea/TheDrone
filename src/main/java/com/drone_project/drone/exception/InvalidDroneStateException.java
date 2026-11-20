package com.drone_project.drone.exception;

public class InvalidDroneStateException extends RuntimeException {
    public InvalidDroneStateException(String message) {
        super(message);
    }
}
