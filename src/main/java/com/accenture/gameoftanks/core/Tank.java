package com.accenture.gameoftanks.core;

public class Tank {

    private int ID;
    private int startingHp = 100;
    private int currentHp;
    private boolean live = false;
    private Position position;
    private Intent intent;
    private float thrust;
    private float mass;
    private float maxSpeed;
    final float width;
    final float length;

    public Tank(float width, float length) {
        this.width = width;
        this.length = length;
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


