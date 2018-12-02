package com.accenture.gameoftanks.core;

import java.io.Serializable;

public class Vehicle extends Entity implements Serializable {

    private int id;
    private int startingHp;
    private int currentHp;

    // translational parameters
    private final float thrust;
    private final float maxSpeed;

    // rotational parameters
    private final float torqueXY;
    private final float maxOmega;

    Vehicle(int id,
            float mass,
            float momentOfInertia,
            float thrust,
            float maxSpeed,
            float torqueXY,
            float maxOmega,
            int startingHp) {
        super(false, mass, momentOfInertia);
        this.id = id;
        this.thrust = thrust;
        this.maxSpeed = maxSpeed;
        this.torqueXY = torqueXY;
        this.maxOmega = maxOmega;
        this.startingHp = startingHp;
        this.currentHp = startingHp;
    }

    public int getID() {
        return id;
    }

    public boolean isAlive() {
        return currentHp > 0;
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

    public float getTorqueXY() {
        return torqueXY;
    }

    public float getMaxOmega() {
        return maxOmega;
    }

    void copyPosition(Vehicle vehicle) {
        this.position.copy(vehicle.position);
    }

    void copyIntent(Vehicle vehicle) {
        this.intent.copy(vehicle.intent);
    }
}
