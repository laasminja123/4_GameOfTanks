package com.accenture.gameoftanks.core;

import java.io.Serializable;

public class Tank implements Serializable {

    private int ID;
    private int startingHp = 100;
    private int currentHp;
    private boolean isAlive;
    private final float thrust;
    private final float mass;
    private final float maxSpeed;
    final float width;
    final float length;

    private Position position;
    private Intent intent;

    public Tank(float length, float width) {
        this.width = width;
        this.length = length;

        this.mass = 5000.0f;
        this.thrust = 5000.0f;
        this.maxSpeed = 10;


        this.position = new Position();
        this.intent = new Intent();

        this.isAlive = true;
    }


    public float getThrust() {
        return thrust;
    }

    public float getMass() {
        return mass;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public Position getPosition() {
        return position;

    }

    public Intent getIntent() {
        return intent;
    }

    public int getID() {
        return ID;
    }

    public int getCurrentHp() {
                return currentHp;

    }

    public float getWidth() {
        return width;
    }

    public float getLength() {
        return length;
    }

    public boolean isAlive() {
        return isAlive;
    }

    void copy(Tank tank) {
        this.position.copy(tank.position);
    }

    void copyPosition(Tank tank) {
        this.position.copy(tank.position);
    }

    void copyIntent(Tank tank) {
        this.intent.copy(tank.intent);
    }
}


