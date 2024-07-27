package com.drone_project.drone.repository;

import com.drone_project.drone.model.Drone;
import com.drone_project.drone.model.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MedicationRepository extends JpaRepository<Medication, Long> {
    List<Medication> findByDrone(Drone drone);
}