package com.accenture.gameoftanks.server.net;

import com.accenture.gameoftanks.net.Data;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.net.Socket;

public class PlayerHandler extends Thread {

    public Socket socket;
    private ObjectInputStream streamIn;
    private ObjectOutputStream streamOut;
    private Data data;

    public PlayerHandler(Socket socket) {
        this.socket = socket;

        try {
            this.streamIn = new ObjectInputStream(socket.getInputStream());
            this.streamOut = new ObjectOutputStream(socket.getOutputStream());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        // wait for data from client loop
        while (true) {
            try {
                Object object = streamIn.readObject();

                if (object instanceof Data) {
                    this.data = (Data) object;
                }
            } catch (IOException | ClassNotFoundException exc) {
                //
            }
        }
    }

    public void sendData() {
        System.out.println("Server sending data to Player");
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

    public Data getData() {
        return this.data;
    }

}
