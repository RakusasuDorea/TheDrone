package com.drone_project.drone.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Model model;

    @Column(nullable = false)
    private int weightLimit;

    @Column(nullable = false)
    private int batteryCapacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State state;

    @OneToMany(mappedBy = "drone")
    @JsonManagedReference
    private List<Medication> medications;

    @PrePersist
    @PreUpdate
    public void setWeightLimitBasedOnModel() {
        if (model != null) {
            this.weightLimit = model.getWeightLimit();
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public int getWeightLimit() {
        return weightLimit;
    }

    public void setWeightLimit(int weightLimit) {
        this.weightLimit = weightLimit;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }
}
