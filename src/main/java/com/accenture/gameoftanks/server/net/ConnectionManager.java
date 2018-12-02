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
    private final int TIME_STEP_MSEC = 40;

    private ServerSocket server;
    private Vector<PlayerHandler> connections;
    private DatabaseManager databaseManager;

    private Level level;
    private Data data;

    private volatile boolean onDemand;

    public ConnectionManager(Level level, DatabaseManager databaseManager) {
        this.connections = new Vector<>();
        this.level = level;
        this.data = new Data();
        this.databaseManager = databaseManager;
    }

    @Override
    public void run() {

        // send data to  client loop
        Thread outputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                onDemand = true;

                while (onDemand) {
                    try {
                        Thread.sleep(TIME_STEP_MSEC);
                    } catch (InterruptedException exc) {
                        exc.printStackTrace();
                    }
                    sendData();
                }
            }
        });
        outputThread.start();

        //listen to new connections loop
        Thread mainLoop = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new ServerSocket(PORT);
                    System.out.println("Server Socket created!");

                    while (onDemand) {
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
            outputThread.join();
            mainLoop.join();
        } catch (InterruptedException exc) {
            exc.printStackTrace();
            onDemand = false;
        }
    }

    private void sendData() {
        data.clear();

        // compose Data object with all current players
        for (PlayerHandler handler: connections) {
            Player player = handler.getPlayer();

            if (player != null) {
                data.addPlayer(player);
            }
        }

        // send data to all players
        for (PlayerHandler handler: connections) {
            handler.sendData(data);
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
            Player player = handler.getPlayer();

            if (player != null) {
                players.add(player);
            }
        }
        return players;
    }

    void removePlayer(PlayerHandler handler) {
        if (databaseManager != null) {
            databaseManager.AddOrUpdatePlayer(handler.getPlayer());
            // TODO call database manager method which informs it about player disconnection
        }
        connections.remove(handler);
    }

    boolean playerExists(Player newPlayer) {
        for (PlayerHandler handler : connections) {
            Player existingPlayer = handler.getPlayer();

            if (existingPlayer != null && newPlayer != null) {
                return existingPlayer.getNickname().equals(newPlayer.getNickname());
            }
        }
        return false;
    }

}
