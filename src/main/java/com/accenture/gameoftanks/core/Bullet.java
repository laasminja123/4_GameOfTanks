package com.accenture.gameoftanks.core;

public class Bullet {

    private final float mass;
    private final float power;
    private final float velocity;

    private float posX;
    private float posY;
    private float angle;

    public Bullet(float mass, float power, float velocity) {
        this.mass = mass;
        this.power = power;
        this.velocity = velocity;
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

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getAngle() {
        return angle;
    }
}
