package com.accenture.gameoftanks.server.net;

import com.accenture.gameoftanks.core.Player;
import com.accenture.gameoftanks.core.Position;
import com.accenture.gameoftanks.net.Data;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerHandler extends Thread {

    private ConnectionManager connectionManager;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Player player;

    PlayerHandler(ConnectionManager connectionManager, Socket socket) {
        this.connectionManager = connectionManager;
        this.socket = socket;

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
        System.out.println("On Player setup...: " + player.getNickname());

        if (connectionManager.playerExists(player)) {
            System.out.println("Player " + player.getNickname() + " exists");
            closeConnection();
            return;
        }
        System.out.println("Client \"" + player.getNickname() + "\" connected to server!");

        float posX = 10.0f;
        float posY = 10.0f;

        Position position = player.getVehicle().getPosition();
        position.posX = posX;
        position.posY = posY;
        position.alpha = 0.0f;
        position.vx = 0.0f;
        position.vy = 0.0f;
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
