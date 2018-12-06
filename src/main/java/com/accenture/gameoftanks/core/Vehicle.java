package com.accenture.gameoftanks.core;

import java.io.Serializable;

public abstract class Vehicle extends Entity implements Serializable {

    // translational parameters
    private final float thrust;
    private final float maxSpeed;

    // rotational parameters
    private final float torqueXY;
    private final float maxOmega;

    // shoot params
    final int shootDelayMsec;
    int currentDelayMsec;

    Vehicle(float mass,
            float momentOfInertia,
            float thrust,
            float maxSpeed,
            float torqueXY,
            float maxOmega,
            int startingHp,
            int shootDelayMsec) {
        super(false, true, mass, momentOfInertia, startingHp);
        this.thrust = thrust;
        this.maxSpeed = maxSpeed;
        this.torqueXY = torqueXY;
        this.maxOmega = maxOmega;
        this.shootDelayMsec = shootDelayMsec;
        currentDelayMsec = 0;
    }

    public abstract void setShootingAngle(float angle);

    public abstract float getShootingAngle();

    public abstract void incrementShootingAngle(float angle);

    public abstract void incrementShootingDelay(int deltaMsec);

    public abstract void resetShootingDelay();

    public abstract int getShootingDelayMsec();

    public abstract int getCurrentDelayMsec();

    public abstract float getBulletMass();

    public abstract float getBulletPower();

    public abstract float getBulletVelocity();

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
        this.setId(vehicle.getId());
        this.setShootingAngle(vehicle.getShootingAngle());
        this.position.copy(vehicle.position);
    }

    void copyData(Vehicle vehicle) {
        this.currentHp = vehicle.currentHp;
        this.currentDelayMsec = vehicle.currentDelayMsec;
    }

    void copyIntent(Vehicle vehicle) {
        this.intent.copy(vehicle.intent);
    }
}
