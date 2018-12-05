package com.accenture.gameoftanks.net;

import com.accenture.gameoftanks.core.Bullet;
import com.accenture.gameoftanks.core.Player;
import com.accenture.gameoftanks.core.Vehicle;

import java.io.Serializable;
import java.util.*;

public class Data implements Serializable {
    private Map<String, Player> players;
    private List<Bullet> bullets;

    public Data() {
        players = new HashMap<>();
        bullets = new LinkedList<>();
    }

    public Data(Player player) {
        this();
        players.put(player.getNickname(), player);
    }

    public void clear() {
        players.clear();
        bullets.clear();
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

    public void copyContent(Data data) {
        this.bullets.clear();
        this.bullets.addAll(data.bullets);
    }

    // removes disconnected players
    public void cleanUp(Data data) {
        Set<String> thisNames = players.keySet();

        if (thisNames.size() == 1) {
            return;
        }

        for (String name: thisNames) {
            if (!data.players.containsKey(name)) {
                players.remove(name);
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

    public int getVehicleId(String playerName) {
        return (players.containsKey(playerName)) ? players.get(playerName).getVehicle().getId() : -1;
    }

    public void addBullets(List<Bullet> bullets) {
        this.bullets.addAll(bullets);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }
}
