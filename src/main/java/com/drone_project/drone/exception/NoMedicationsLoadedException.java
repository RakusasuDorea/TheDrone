package com.drone_project.drone.exception;

public class NoMedicationsLoadedException extends RuntimeException {
    public NoMedicationsLoadedException(String message) {
        super(message);
    }
}
