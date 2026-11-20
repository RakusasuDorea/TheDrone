package com.drone_project.drone;

import com.drone_project.drone.repository.DroneRepository;
import com.drone_project.drone.repository.MedicationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class DroneApplicationTests {

    @MockBean
    private DroneRepository droneRepository;

    @MockBean
    private MedicationRepository medicationRepository;

    @Test
    void contextLoads() {
    }
}
