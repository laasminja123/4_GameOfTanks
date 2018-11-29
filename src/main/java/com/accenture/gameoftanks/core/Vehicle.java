package com.accenture.gameoftanks.core;

import java.io.Serializable;

public class Vehicle extends Entity implements Serializable {

    private int id;
    private int startingHp;
    private int currentHp;

    private final float thrust;
    private final float maxSpeed;

    Vehicle(int id, float mass, float thrust, float maxSpeed, int startingHp) {
        super(false, mass);
        this.id = id;
        this.thrust = thrust;
        this.maxSpeed = maxSpeed;
        this.startingHp = startingHp;
        this.currentHp = startingHp;
    }

    public int getID() {
        return id;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public float getThrust() {
        return thrust;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    void copy(Vehicle vehicle) {
        this.position.copy(vehicle.position);
    }

    void copyPosition(Vehicle vehicle) {
        this.position.copy(vehicle.position);
    }

    void copyIntent(Vehicle vehicle) {
        this.intent.copy(vehicle.intent);
    }
}
