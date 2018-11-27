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

}
