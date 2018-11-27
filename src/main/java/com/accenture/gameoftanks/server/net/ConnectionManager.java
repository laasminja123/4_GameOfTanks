package com.accenture.gameoftanks.server.net;

import com.accenture.gameoftanks.client.Session;
import com.accenture.gameoftanks.core.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class ConnectionManager extends Thread{

    private int port; // port to listen on
    private ServerSocket server;
    private Vector<PlayerHandler> connections; // change from Session to PlayerConnection and don't forget to import it as well

    public ConnectionManager() {
        this.port = 9999;
        this.connections = new Vector<PlayerHandler>();
        try {
            this.server = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = this.server.accept();
                PlayerHandler playerHandler = new PlayerHandler(socket);
                this.connections.add(playerHandler);
                new Thread(playerHandler).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Player> getPlayers() {
        List<Player> players = new LinkedList<>();
        return players;
    }

}
