package com.accenture.gameoftanks.core;

import java.io.Serializable;

public class Position implements Serializable {
    public volatile float posX;
    public volatile float posY;
    public volatile float alpha;
    public volatile float vx;
    public volatile float vy;

//Create new constructor
    public Position() {}

    void copy(Position position) {
        this.posX = position.posX;
        this.posY = position.posY;
        this.alpha = position.alpha;
        this.vx = position.vx;
        this.vy = position.vy;
    }
}
