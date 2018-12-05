package com.accenture.gameoftanks.core;

import java.io.Serializable;

public class Bullet implements Serializable {

    private final float mass;
    private final float power;
    private final float velocity;

    private int vehicleId;

    public volatile float posX;
    public volatile float posY;
    public volatile float angle;

    public volatile boolean isConsumed;

    public Bullet(int vehicleId, float mass, float power, float velocity) {
        this.vehicleId = vehicleId;
        this.mass = mass;
        this.power = power;
        this.velocity = velocity;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public float getMass() {
        return mass;
    }

    public float getPower() {
        return power;
    }

    public float getVelocity() {
        return velocity;
    }
}
