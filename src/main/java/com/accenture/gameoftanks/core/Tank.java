package com.accenture.gameoftanks.core;

import java.io.Serializable;

public class Tank implements Serializable {

    private int ID;
    private int startingHp = 100;
    private int currentHp;
    private boolean live = false;
    final int thrust = 5000;
    final int mass = 5000;
    private float maxSpeed = 10;
    final float width;
    final float length;

    private Position position;
    private Intent intent;

    public Tank(float length, float width) {
        this.width = width;
        this.length = length;

        this.position = new Position();
        this.intent = new Intent();
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

    public void isAlive(boolean live) {
                if (!live) {
                    return;
        }
    }
}


