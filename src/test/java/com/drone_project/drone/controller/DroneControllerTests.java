package com.drone_project.drone.controller;

import com.drone_project.drone.model.Drone;
import com.drone_project.drone.service.DroneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class DroneControllerTests {

    @InjectMocks
    private DroneController droneController;

    @Mock
    private DroneService droneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDroneInfo_PositiveTest() {
        // Arrange
        Drone expectedDrone = new Drone();
        expectedDrone.setSerialNumber("123");
        when(droneService.getDroneInfo("123")).thenReturn(expectedDrone);

        // Act
        ResponseEntity<Drone> response = droneController.getDroneInfo("123");

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedDrone, response.getBody());
    }
    @Test
    void testGetDroneInfo_NegativeTest() {

        when(droneService.getDroneInfo("123")).thenReturn(null);

        ResponseEntity<Drone> response = droneController.getDroneInfo("123");

        assertEquals(404, response.getStatusCodeValue());
    }
}
