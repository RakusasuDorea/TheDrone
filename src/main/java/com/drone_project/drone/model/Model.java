package com.drone_project.drone.model;

public enum Model {
    Lightweight(250),
    Middleweight(500),
    Cruiserweight(750),
    Heavyweight(1000);

    private final int weightLimit;

    Model(int weightLimit) {
        this.weightLimit = weightLimit;
    }

    public int getWeightLimit() {
        return weightLimit;
    }
}
