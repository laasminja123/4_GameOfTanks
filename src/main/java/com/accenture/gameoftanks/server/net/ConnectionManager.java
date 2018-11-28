package com.accenture.gameoftanks.server.net;

import com.accenture.gameoftanks.core.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class ConnectionManager extends Thread {

    public static final int PORT = 9999;
    public static final int TIME_STEP_MSEC = 1000;

    private ServerSocket server;
    private Vector<PlayerHandler> connections;

    public ConnectionManager() {
        this.connections = new Vector<>();
    }

    @Override
    public void run() {
        //listen to new connections loop
        Thread thread = new Thread(new Runnable() {
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
                        } catch (IOException exc) {
                            exc.printStackTrace();
                        }
                    }
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }
        });
        thread.start();

        // send data to all clients loop
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(TIME_STEP_MSEC);
                    } catch (InterruptedException exc) {
                        exc.printStackTrace();
                    }

                    for (PlayerHandler handler: connections) {
                        handler.sendData();
                    }
                }
            }
        });
        thread2.start();

        try {
            thread.join();
            thread2.join();
        } catch (InterruptedException exc) {
            exc.printStackTrace();
        }
    }

    public List<Player> getPlayers() {
        List<Player> players = new LinkedList<>();

        for (PlayerHandler handler : connections) {
            players.add(handler.getData().getPlayer());
        }
        return players;
    }

    void removePlayer(PlayerHandler handler) {
        connections.remove(handler);
    }

}
