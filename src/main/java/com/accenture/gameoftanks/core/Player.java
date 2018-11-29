package com.accenture.gameoftanks.core;

import java.io.Serializable;

public class Player implements Serializable {

    private int id;
    private Tank tank;
    private final String nickname;

    public Player(String nickname) {
        this.nickname = nickname;
        this.tank = new Tank(5.0f, 3.0f);
    }

    public int getId() {
        return id;
    }

    public Tank getTank() {
        return tank;
    }

    public String getNickname() {
        return nickname;
    }

    public void copy(Player player) {
        this.tank.copy(player.tank);
    }

    public void copyPosition(Player player) {
        this.tank.copyPosition(player.tank);
    }

    public void copyIntent(Player player) {
        this.tank.copyIntent(player.tank);
    }
}
