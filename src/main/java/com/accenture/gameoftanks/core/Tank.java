package com.accenture.gameoftanks.core;

public class Tank {

    private int startingHp = 100;
    private int currentHp;
    private boolean live = false;
    private Position position;
    private Intent intent;



    public Position getPosition() {
        return position;

    }

    public Intent getIntent() {
        return intent;
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


