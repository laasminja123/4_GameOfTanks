package com.accenture.gameoftanks.server.net;

import com.accenture.gameoftanks.core.Bullet;
import com.accenture.gameoftanks.core.Level;
import com.accenture.gameoftanks.core.Player;
import com.accenture.gameoftanks.net.Data;
import com.accenture.gameoftanks.server.core.DataCore;
import com.accenture.gameoftanks.server.core.DatabaseManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ConnectionManager extends Thread {

    public static final int PORT = 9999;
    private final int TIME_STEP_MSEC = 40;

    private ServerSocket server;
    private Vector<PlayerHandler> connections;
    private DataCore dataCore;
    private DatabaseManager databaseManager;

    private Level level;
    private Data data;

    private int vehicleId;

    private volatile boolean onDemand;

    public ConnectionManager(Level level, DatabaseManager databaseManager) {
        this.connections = new Vector<>();
        this.level = level;
        this.data = new Data();
        this.databaseManager = databaseManager;
        this.vehicleId = 1;
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
                            PlayerHandler playerHandler = new PlayerHandler(ConnectionManager.this, socket, vehicleId++);
                            playerHandler.start();
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

    public void setDataCore(DataCore dataCore) {
        this.dataCore = dataCore;
    }

    private void sendData() {
        data.clear();

        // compose Data object with all current players
        for (PlayerHandler handler : connections) {
            Player player = handler.getPlayer();

            if (player != null) {
                data.addPlayer(player);
            }
        }

        // add information about existing bullets
        if (dataCore != null) {
            List<Bullet> bullets = dataCore.getBullets();
            List<Bullet> toRemove = new LinkedList<>();

            // clean up list
            for (Bullet bullet : bullets) {
                if (bullet.isConsumed) {
                    toRemove.add(bullet);
                }
            }
            bullets.removeAll(toRemove);

            data.addBullets(bullets);
        }

        // send data to all players
        for (PlayerHandler handler : connections) {
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

    void addNewPlayer(PlayerHandler handler) {
        this.connections.add(handler);
        System.out.println("Connections pull size is: " + connections.size());
    }

    void removePlayer(PlayerHandler handler) {
        if (databaseManager != null) {
            databaseManager.addOrUpdatePlayer(handler.getPlayer());
            databaseManager.logDisconnect(handler.getPlayer());
        }
        connections.remove(handler);
        System.out.println("Connections pull size is: " + connections.size());
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

    public void stopService() {
        connections.clear();
        dataCore = null;
        onDemand = false;

        try {
            server.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
