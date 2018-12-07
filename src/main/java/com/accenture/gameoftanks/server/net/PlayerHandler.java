package com.accenture.gameoftanks.server.net;

import com.accenture.gameoftanks.core.Level;
import com.accenture.gameoftanks.core.Player;
import com.accenture.gameoftanks.core.Position;
import com.accenture.gameoftanks.net.Data;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Queue;

public class PlayerHandler extends Thread {

    private ConnectionManager connectionManager;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Player player;

    private int vehicleId;

    PlayerHandler(ConnectionManager connectionManager, Socket socket, int vehicleId) {
        this.connectionManager = connectionManager;
        this.socket = socket;
        this.vehicleId = vehicleId;

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (java.io.IOException exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public void run() {
        // receive packages from client
        while (true) {
            try {
                Object object = inputStream.readObject();

                if (object instanceof Data) {
                    Data data = (Data) object;

                    if (player == null) {
                        // first data transfer
                        Player newPlayer = data.getPlayer();

                        if (newPlayer != null) {
                            setupPlayer(newPlayer);
                            player = newPlayer;
                        }
                    } else {
                        player.copyIntent(data.getPlayer(player.getNickname()));
                    }
                }
            } catch (IOException | ClassNotFoundException exc) {
                closeConnection();
                break;
            }
        }
    }

    private void setupPlayer(Player player) {

        if (connectionManager.getDatabaseManager() != null) {
            connectionManager.getDatabaseManager().logConnect(player);
        }

        if (connectionManager.playerExists(player)) {
            System.out.println("Player " + player.getNickname() + " already exists");
            closeConnection();
            return;
        }

        // compute most suitable respawn point for a new player
        float [] respawn = computeRespawn();

        // add new player to pull
        connectionManager.addNewPlayer(this);

        System.out.println("Client \"" + player.getNickname() + "\" connected to server!");

        Position position = player.getVehicle().getPosition();
        position.posX = respawn[0];
        position.posY = respawn[1];
        position.alpha = respawn[2];
        position.vx = 0.0f;
        position.vy = 0.0f;

        player.getVehicle().setId(vehicleId);
    }

    private float [] computeRespawn() {
        Queue<Player> players = connectionManager.getPlayers();
        Level level = connectionManager.getLevel();
        return level.getRespawn(players);
    }

    void sendData(Data data) {
        if (inputStream == null || outputStream == null || data == null) {
            return;
        }

        try {
            outputStream.reset();
            outputStream.writeObject(data);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    Player getPlayer() {
        return this.player;
    }

    private void closeConnection() {
        connectionManager.removePlayer(this);

        if (player != null) {
            System.out.println("Client \"" + player.getNickname() + "\" disconnected!");
        }

        try {
            outputStream.close();
            inputStream.close();
            socket.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
