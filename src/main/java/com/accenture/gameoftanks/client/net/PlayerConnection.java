package com.accenture.gameoftanks.client.net;

import com.accenture.gameoftanks.core.Player;
import com.accenture.gameoftanks.net.Data;
import com.accenture.gameoftanks.server.net.ConnectionManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerConnection {

    public static final int TIME_STEP_MSEC = 50;

    private Player player;
    public Socket client;
    private ObjectInputStream streamIn;
    private ObjectOutputStream streamOut;
    private Data data;

    public PlayerConnection(Player player) {
        this.player = player;

        try {
            client = new Socket("localhost", ConnectionManager.PORT);
            this.streamIn = new ObjectInputStream(client.getInputStream());
            this.streamOut = new ObjectOutputStream(client.getOutputStream());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        this.data = new Data(player);

        // create data sending loop
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(TIME_STEP_MSEC);
                    } catch (InterruptedException exc) {
                        //
                    }

                    sendData();
                }
            }
        });
        thread.start();
    }

    public void sendData() {
        if (streamIn == null || streamOut == null || data == null) {
            return;
        }
        try {
            streamOut.flush();
            streamOut.writeObject(this.data);
        } catch (IOException exc) {
            //
        }
    }

}
