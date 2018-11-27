package com.accenture.gameoftanks.core;

public class Tank {

    private int startingHp = 100;
    private int currentHp;
    private boolean live = false;
    private Position position;


    public Position getPosition() {
        return position;

    }


    public int getCurrentHp() {
        return currentHp;

    }

        public void isAlive(boolean live) {
        if (!live) {
        return;
        }
    }
}


