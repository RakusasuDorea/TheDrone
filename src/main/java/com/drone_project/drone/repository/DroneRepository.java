package com.drone_project.drone.repository;

import com.drone_project.drone.model.Drone;
import com.drone_project.drone.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Long> {
    Drone findBySerialNumber(String serialNumber);

    List<Drone> findByState(State state);
}
