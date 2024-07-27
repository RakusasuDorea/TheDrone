package com.drone_project.drone.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import javax.validation.constraints.Pattern;


@Entity
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[a-zA-Z0-9-_]+$", message = "Name can only contain letters, numbers, dashes, and underscores.")
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int weight;

    @Pattern(regexp = "^[A-Z0-9_]+$", message = "Code can only contain uppercase letters, numbers, and underscores.")
    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String image;

    @ManyToOne
    @JoinColumn(name = "drone_id")
    @JsonBackReference
    private Drone drone;


    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public Drone getDrone() {
        return drone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
