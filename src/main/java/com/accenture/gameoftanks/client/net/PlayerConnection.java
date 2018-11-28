package com.accenture.gameoftanks.client.net;

import com.accenture.gameoftanks.core.Player;
import com.accenture.gameoftanks.net.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerConnection {

    public static final int TIME_STEP_MSEC = 1000;

    private Player player;
    public Socket client;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Data data;
    private String host;
    private int port;

    public PlayerConnection(String host, int port, Player player) {
        this.host = host;
        this.port = port;
        this.player = player;
    }

    public void init() {
        try {
            client = new Socket(host, port);
            outputStream = new ObjectOutputStream(client.getOutputStream());
            inputStream = new ObjectInputStream(client.getInputStream());
        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect server");
        }
        this.data = new Data(player);

        // send data to server loop
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(TIME_STEP_MSEC);
                    } catch (InterruptedException exc) {
                        exc.printStackTrace();
                    }
                    sendData();
                }
            }
        });
        thread.start();

        // create data receiving loop
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Object object = inputStream.readObject();

                        if (object instanceof Data) {
                            data = (Data) object;
                            //System.out.println("Player received package from Server...");
                        }
                    } catch (IOException | ClassNotFoundException exc) {
                        exc.printStackTrace();
                    }
                }
            }
        });
        thread2.start();
    }

    public void sendData() {
        //System.out.println("Player sending data to Server");

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

    public void disconnect() {
        try {
            inputStream.close();
            outputStream.close();
            client.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

}
