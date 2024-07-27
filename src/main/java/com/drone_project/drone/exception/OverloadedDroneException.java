package com.drone_project.drone.exception;

public class OverloadedDroneException extends RuntimeException {
    public OverloadedDroneException(String message) {
        super(message);
    }
}
