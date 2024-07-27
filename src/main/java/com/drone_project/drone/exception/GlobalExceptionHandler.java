//package com.drone_project.drone.exception;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
//    }
//
//    @ExceptionHandler(RuntimeException.class)
//    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(e.getMessage());
//    }
//
//    @ExceptionHandler(OverloadedDroneException.class)
//    public ResponseEntity<String> handleOverloadedDroneException(OverloadedDroneException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(e.getMessage());
//    }
//
//    @ExceptionHandler(InsufficientBatteryException.class)
//    public ResponseEntity<String> handleInsufficientBatteryException(InsufficientBatteryException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body(e.getMessage());
//    }
//}
