package com.accenture.gameoftanks.server.net;

import com.accenture.gameoftanks.net.Data;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerHandler extends Thread {

    private ConnectionManager connectionManager;
    public Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Data data;

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
        // wait for data from client loop
        while (true) {
            try {
                Object object = inputStream.readObject();

                if (object instanceof Data) {
                    Data data = (Data) object;

                    if (this.data == null) {
                        System.out.println("Client \"" + data.getPlayer().getNickname() + "\" connected to server!");
                    }
                    this.data = data;
                }
            } catch (IOException | ClassNotFoundException exc) {
                //exc.printStackTrace();
                connectionManager.removePlayer(this);
                System.out.println("Client \"" + data.getPlayer().getNickname() + "\" disconnected!");
                break;
            }
        }
    }

    public void sendData() {
        System.out.println("Server sending data to Player");

        if (inputStream == null || outputStream == null || data == null) {
            return;
        }

        try {
            outputStream.flush();
            outputStream.writeObject(this.data);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

    public Data getData() {
        return this.data;
    }

}
