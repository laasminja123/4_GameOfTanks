package com.accenture.gameoftanks.core;

public class Player {

    private String ID;
    private Tank tank;
    private final String nickname;

    public Player(String nickname) {
        this.nickname = nickname;
        this.tank = new Tank(5.0f, 3.0f);
    }

    public String getID() {
        return ID;
    }

    public Tank getTank() {
        return tank;
    }

    public String getNickname() {
        return nickname;
    }
}
