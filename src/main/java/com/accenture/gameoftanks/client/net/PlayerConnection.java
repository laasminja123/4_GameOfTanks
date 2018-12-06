package com.accenture.gameoftanks.client.net;

import com.accenture.gameoftanks.core.Level;
import com.accenture.gameoftanks.net.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class PlayerConnection {

    private static final int TIME_STEP_MSEC = 40;

    private Socket client;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final Data data;
    private final Level level;
    private String host;
    private int port;

    private boolean onDemand;

    public PlayerConnection(String host, int port, Data data, Level level) {
        this.host = host;
        this.port = port;
        this.data = data;
        this.level = level;
    }

    public void init() {
        try {
            client = new Socket(host, port);
            outputStream = new ObjectOutputStream(client.getOutputStream());
            inputStream = new ObjectInputStream(client.getInputStream());
        } catch (java.io.IOException e) {
            e.printStackTrace();
            disconnect();
            throw new RuntimeException("Failed to connect server");
        }
        onDemand = true;

        // send data to server loop
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
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
        thread.start();

        // create data receiving loop
        Thread receiveLoop = new Thread(new Runnable() {
            @Override
            public void run() {
                while (onDemand) {
                    try {
                        Object object = inputStream.readObject();

                        if (object instanceof Data) {
                            Data data = (Data) object;

                            synchronized (PlayerConnection.this.data) {
                                PlayerConnection.this.data.copyPosition(data);
                                PlayerConnection.this.data.copyContent(data);
                                PlayerConnection.this.data.copyScores(data);
                                PlayerConnection.this.data.copyVehicleData(data);
                                PlayerConnection.this.data.cleanUp(data);
                            }

                            synchronized (PlayerConnection.this.level) {
                                PlayerConnection.this.level.copyDeadObjects(data.getLevelDeadObjects());
                            }
                        }
                    } catch (IOException | ClassNotFoundException exc) {
                        //exc.printStackTrace();
                        disconnect();
                    }
                }
            }
        });
        receiveLoop.start();
    }

    private void sendData() {
        if (inputStream == null || outputStream == null || data == null) {
            return;
        }

        try {
            outputStream.reset();
            outputStream.writeObject(data);
        } catch (IOException exc) {
            //exc.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            inputStream.close();
            outputStream.close();
            client.close();
        } catch (IOException exc) {
            exc.printStackTrace();
        } finally {
            onDemand = false;
        }
    }

    public boolean isAlive() {
        return onDemand;
    }
}
