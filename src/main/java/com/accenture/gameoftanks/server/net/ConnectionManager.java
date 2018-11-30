package com.accenture.gameoftanks.server.net;

import com.accenture.gameoftanks.core.Level;
import com.accenture.gameoftanks.core.Player;
import com.accenture.gameoftanks.net.Data;
import com.accenture.gameoftanks.server.core.DatabaseManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class ConnectionManager extends Thread {

    public static final int PORT = 9999;

    private ServerSocket server;
    private Vector<PlayerHandler> connections;
    private DatabaseManager databaseManager;

    private Level level;

    public ConnectionManager(Level level, DatabaseManager databaseManager) {
        this.connections = new Vector<>();
        this.level = level;
        this.databaseManager = databaseManager;
    }

    @Override
    public void run() {
        //listen to new connections loop
        Thread mainLoop = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new ServerSocket(PORT);
                    System.out.println("Server Socket created!");

                    while (true) {
                        try {
                            Socket socket = server.accept();
                            System.out.println("Player connected");
                            PlayerHandler playerHandler = new PlayerHandler(ConnectionManager.this, socket);
                            connections.add(playerHandler);
                            playerHandler.start();
                            // TODO call database method which informs it about player connection
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }
                    }
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        });
        mainLoop.start();

        try {
            mainLoop.join();
        } catch (InterruptedException exc) {
            exc.printStackTrace();
        }
    }

    public Level getLevel() {
        return level;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public List<Player> getPlayers() {
        List<Player> players = new LinkedList<>();

        for (PlayerHandler handler : connections) {
            Data data = handler.getData();

            if (data != null) {
                players.add(data.getPlayer());
            }
        }
        return players;
    }

    void removePlayer(PlayerHandler handler) {
        if (databaseManager != null) {
            databaseManager.AddOrUpdatePlayer(handler.getData().getPlayer());
            // TODO call database manager method which informs it about player disconnection
        }
        connections.remove(handler);
    }

    public boolean playerExists(Player player) {
        for (PlayerHandler handler : connections) {
            Data data = handler.getData();
            if (data.getPlayer().getNickname().equals(player.getNickname())) {
                return true;
            }
        }
        return false;
    }

}
