package com.accenture.gameoftanks.core;

import java.io.Serializable;

public class Position implements Serializable {
    public volatile float posX;
    public volatile float posY;
    public volatile float vx;  // velocity x component
    public volatile float vy;  // velocity y component
    public volatile float alpha;  // rotation angle in radians relative to X axis (counter-clockwise is positive)
    public volatile float omega;  // angular velocity in 1/s

//Create new constructor
    public Position() {}

    void copy(Position position) {
        this.posX = position.posX;
        this.posY = position.posY;
        this.vx = position.vx;
        this.vy = position.vy;
        this.alpha = position.alpha;
        this.omega = position.omega;
    }
}
