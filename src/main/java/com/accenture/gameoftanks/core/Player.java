package com.accenture.gameoftanks.core;

import java.io.Serializable;

public class Player implements Serializable {

    private int id;
    private Vehicle vehicle;
    private final String nickname;

    public Player(String nickname) {
        this.nickname = nickname;
        this.vehicle = new Tank(0, 5.0f, 3.0f);
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

    public void copy(Player player) {
        this.vehicle.copy(player.vehicle);
    }

    public void copyPosition(Player player) {
        this.vehicle.copyPosition(player.vehicle);
    }

    public void copyIntent(Player player) {
        this.vehicle.copyIntent(player.vehicle);
    }
}
