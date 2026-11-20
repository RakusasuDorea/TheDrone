package com.drone_project.drone.exception;

import com.drone_project.drone.dto.ErrorResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DroneNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleDroneNotFoundException(DroneNotFoundException e) {
        return build(HttpStatus.NOT_FOUND, "Drone Not Found", e.getMessage());
    }

    @ExceptionHandler(OverloadedDroneException.class)
    public ResponseEntity<ErrorResponseDTO> handleOverloadedDroneException(OverloadedDroneException e) {
        return build(HttpStatus.BAD_REQUEST, "Overloaded Drone", e.getMessage());
    }

    @ExceptionHandler(InsufficientBatteryException.class)
    public ResponseEntity<ErrorResponseDTO> handleInsufficientBatteryException(InsufficientBatteryException e) {
        return build(HttpStatus.BAD_REQUEST, "Insufficient Battery", e.getMessage());
    }

    @ExceptionHandler(NoMedicationsLoadedException.class)
    public ResponseEntity<ErrorResponseDTO> handleNoMedicationsLoadedException(NoMedicationsLoadedException e) {
        return build(HttpStatus.BAD_REQUEST, "No Medications Loaded", e.getMessage());
    }

    @ExceptionHandler(MedicationsLoadedException.class)
    public ResponseEntity<ErrorResponseDTO> handleMedicationsLoadedException(MedicationsLoadedException e) {
        return build(HttpStatus.BAD_REQUEST, "Medications Still Loaded", e.getMessage());
    }

    @ExceptionHandler(InvalidDroneStateException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidDroneStateException(InvalidDroneStateException e) {
        return build(HttpStatus.BAD_REQUEST, "Invalid Drone State", e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        String message = e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage();

        if (message != null && message.contains("serial_number")) {
            return build(HttpStatus.CONFLICT, "Duplicate Serial Number", "A drone with this serial number already exists.");
        }
        if (message != null && message.contains("code")) {
            return build(HttpStatus.CONFLICT, "Duplicate Medication Code", "A medication with this code already exists.");
        }

        return build(HttpStatus.CONFLICT, "Duplicate Entry", "A record with the same unique value already exists.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return build(HttpStatus.BAD_REQUEST, "Validation Failed", message);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception e) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
    }

    private ResponseEntity<ErrorResponseDTO> build(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status)
                .body(new ErrorResponseDTO(status.value(), error, message, LocalDateTime.now()));
    }
}
