package com.accenture.gameoftanks.net;

import com.accenture.gameoftanks.core.Player;

import java.io.Serializable;

public class Data implements Serializable {
    private Player player;

    public Data(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void copy(Data data) {
        this.player.copy(data.player);
    }

    public void copyPosition(Data data) {
        this.player.copyPosition(data.player);
    }

    public void copyIntent(Data data) {
        this.player.copyIntent(data.player);
    }
}
