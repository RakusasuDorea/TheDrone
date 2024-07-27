package com.drone_project.drone.exception;

public class InsufficientBatteryException extends RuntimeException {
    public InsufficientBatteryException(String message) {
        super(message);
    }
}