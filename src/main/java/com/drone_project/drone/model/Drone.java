package com.drone_project.drone.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "drones")
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "serial_number", length = 100, nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "battery_capacity", nullable = false)
    private int batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "model", nullable = false)
    private Model model;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private State state;

    @OneToMany(mappedBy = "drone")
    @JsonManagedReference
    private List<Medication> medications;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public int getWeightLimit() {
        return model != null ? model.getWeightLimit() : 0;
    }
}
