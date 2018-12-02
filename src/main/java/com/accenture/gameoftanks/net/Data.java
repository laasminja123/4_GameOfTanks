package com.accenture.gameoftanks.net;

import com.accenture.gameoftanks.core.Player;
import com.accenture.gameoftanks.core.Vehicle;

import java.io.Serializable;
import java.util.*;

public class Data implements Serializable {
    private Map<String, Player> players;

    public Data() {
        players = new HashMap<>();
    }

    public Data(Player player) {
        this();
        players.put(player.getNickname(), player);
    }

    public void clear() {
        players.clear();
    }

    public Player getPlayer() {
        List<String> playerNames = new LinkedList<>(players.keySet());

        if (!playerNames.isEmpty()) {
            return players.get(playerNames.get(0));
        }
        return null;
    }

    public Player getPlayer(String nickName) {
        if (nickName == null) {
            return null;
        }
        return (players.containsKey(nickName)) ? players.get(nickName) : null;
    }

    public void addPlayer(Player player) {
        players.put(player.getNickname(), player);
    }

    private Set<String> getPlayerNames() {
        return players.keySet();
    }

    public void copyPosition(Data data) {
        Set<String> playerNames = data.getPlayerNames();

        for (String name: playerNames) {
            if (players.containsKey(name)) {
                players.get(name).copyPosition(data.getPlayer(name));
            } else {
                players.put(name, data.getPlayer(name));
            }
        }
    }

    public List<Vehicle> getVehicles() {
        List<Vehicle> vehicles = new LinkedList<>();
        Set<String> playerNames = players.keySet();

        for (String name: playerNames) {
            vehicles.add(players.get(name).getVehicle());
        }
        return vehicles;
    }
}
