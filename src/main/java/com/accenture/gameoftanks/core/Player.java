package com.accenture.gameoftanks.core;

import java.io.Serializable;

public class Player implements Serializable {

    private int id;
    private Vehicle vehicle;
    private final String nickname;

    private int deaths;
    private int kills;
    private int shoots;

    public Player(String nickname) {
        this.nickname = nickname;
        this.vehicle = new Tank(5.9f, 3.0f);
    }

    public int getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public String getNickname() {
        return nickname;
    }

    public void copyPosition(Player player) {
        if (player != null) {
            this.vehicle.copyPosition(player.vehicle);
        }
    }

    public void copyIntent(Player player) {
        if (player != null) {
            this.vehicle.copyIntent(player.vehicle);
        }
    }

    public int getDeaths() {
        return deaths;
    }

    public int getKills() {
        return kills;
    }

    public int getShoots() {
        return shoots;
    }
}
