package com.accenture.gameoftanks.server.net;

import com.accenture.gameoftanks.client.Session;
import com.accenture.gameoftanks.core.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class ConnectionManager extends Thread {

    public static final int PORT = 9999; // port to listen on
    public static final int TIME_STEP_MSEC = 50;

    private ServerSocket server;
    private Vector<PlayerHandler> connections; // change from Session to PlayerConnection and don't forget to import it as well

    public ConnectionManager() {
        this.connections = new Vector<PlayerHandler>();
        try {
            this.server = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket socket = server.accept();
                        PlayerHandler playerHandler = new PlayerHandler(socket);
                        connections.add(playerHandler);
                        playerHandler.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(TIME_STEP_MSEC);
                    } catch (InterruptedException exc) {
                        //
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
            //
        }
    }

    public List<Player> getPlayers() {
        List<Player> players = new LinkedList<>();
        return players;
    }

}
