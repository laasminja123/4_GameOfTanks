package com.accenture.gameoftanks.core;

public class Turret {

    private float length;
    private float width;
    private float angel;

    public Turret(float length, float width, float angel) {
        this.length = length;
        this.width = width;
        this.angel = 0;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getAngel() {
        return angel;
    }

    public void setAngel(float angel) {
        this.angel = angel;
    }
}