package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Shape;

import java.io.Serializable;

public class Tank extends Vehicle implements Serializable {

    private final float length;
    private final float width;

    private Turret turret;

    Tank(float length, float width) {
        super(5000.0f, 5000.0f, 10000.0f, 8, 2000.0f, 1.3f, 100, 2000);
        Shape topology = new Shape(createTopology(length, width));
        setTopology(topology);

        this.width = width;
        this.length = length;

        this.turret = new Turret(2.0f, 1.5f, .8f, 3.2f, .18f);
    }

    private float [] createTopology(float length, float width) {
        float [] vertices = new float[8];

        vertices[0] = -length / 2.0f;
        vertices[1] = -width / 2.0f;

        vertices[2] = -length / 2.0f;
        vertices[3] =  width / 2.0f;

        vertices[4] =  length / 2.0f;
        vertices[5] =  width / 2.0f;

        vertices[6] =  length / 2.0f;
        vertices[7] = -width / 2.0f;
        return vertices;
    }

    public float getLength() {
        return length;
    }

    public float getWidth() {
        return width;
    }

    @Override
    public void setShootingAngle(float angle) {
        this.turret.setAngle(angle);
    }

    @Override
    public void incrementShootingAngle(float delta) {
        this.turret.incrementTurretAngle(delta);
    }

    @Override
    public void resetShootingDelay() {
        currentDelayMsec = shootDelayMsec;
    }

    @Override
    public float getShootingAngle() {
        return turret.getAngle();
    }

    @Override
    public void incrementShootingDelay(int deltaMsec) {
        currentDelayMsec -= deltaMsec;

        if (currentDelayMsec < 0) {
            currentDelayMsec = 0;
        }
    }

    @Override
    public int getShootingDelayMsec() {
        return shootDelayMsec;
    }

    @Override
    public int getCurrentDelayMsec() {
        return currentDelayMsec;
    }

    @Override
    public float getBulletMass() {
        return 5.0f;  // kg
    }

    @Override
    public float getBulletPower() {
        return 50.0f;
    }

    @Override
    public float getBulletVelocity() {
        return 20.0f;  // m/s
    }

    public Turret getTurret() {
        return turret;
    }
}


