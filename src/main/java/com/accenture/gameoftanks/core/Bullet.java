package com.accenture.gameoftanks.core;

public class Bullet {

    final float MASS;
    final float POWER;
    final float VELOCITY;

    private float posX;
    private float posY;
    private int angle;

    public Bullet(float mass, float power, float velocity) {
        MASS = mass;
        POWER = power;
        VELOCITY = velocity;
    }

    public float getMASS() {
        return MASS;
    }

    public float getPOWER() {
        return POWER;
    }

    public float getVELOCITY() {
        return VELOCITY;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public int getAngle() {
        return angle;
    }



}
