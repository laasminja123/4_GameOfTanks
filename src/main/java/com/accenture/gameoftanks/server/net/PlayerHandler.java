package com.accenture.gameoftanks.server.net;

import com.accenture.gameoftanks.core.Level;
import com.accenture.gameoftanks.core.Position;
import com.accenture.gameoftanks.net.Data;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerHandler extends Thread {

    public static final int TIME_STEP_MSEC = 40;

    private ConnectionManager connectionManager;
    public Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private volatile Data data;

    private volatile boolean onDemand;

    public PlayerHandler(ConnectionManager connectionManager, Socket socket) {
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

        // receive packages from client
        while (true) {
            try {
                Object object = inputStream.readObject();

                if (object instanceof Data) {
                    Data data = (Data) object;

                    if (this.data == null) {
                        // first data exchange
                        setupPlayer(data);
                        this.data = data;
                    } else {
                        this.data.copyIntent(data);
                    }
                    //System.out.println("Value received from Client is: " + data.getPlayer().getTank().getPosition().posX);
                }
            } catch (IOException | ClassNotFoundException exc) {
                //exc.printStackTrace();
                connectionManager.removePlayer(this);
                System.out.println("Client \"" + data.getPlayer().getNickname() + "\" disconnected!");
                onDemand = false;
                break;
            }
        }
    }

    private void setupPlayer(Data data) {

        if (connectionManager.playerExists(data.getPlayer())) {
            connectionManager.removePlayer(this);
            System.out.println("Client \"" + data.getPlayer().getNickname() + "\" disconnected!");
            onDemand = false;
            return;
        }
        connectionManager.addPlayerToDatabase(data.getPlayer());
        System.out.println("Client \"" + data.getPlayer().getNickname() + "\" connected to server!");
        Level level = connectionManager.getLevel();

        float posX = 0.5f * (level.leftBoundary + level.rightBoundary);
        float posY = 0.5f * (level.bottomBoundary + level.topBoundary);

        Position position = data.getPlayer().getTank().getPosition();
        position.posX = posX;
        position.posY = posY;
        position.alpha = 0.0f;
        position.vx = 0.0f;
        position.vy = 0.0f;
    }

    private void sendData() {
        //System.out.println("Server sending data to Player");

        if (inputStream == null || outputStream == null || data == null) {
            return;
        }

        try {
            //System.out.println("value on send is: " + data.getValue());
            //System.out.println("hash is: " + data.hashCode());
            //System.out.println("Value on send is: " + data.getPlayer().getTank().getPosition().posX);
            outputStream.reset();
            outputStream.writeObject(data);
            //outputStream.flush();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public Data getData() {
        return this.data;
    }

}
