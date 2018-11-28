package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.server.net.ConnectionManager;

public class Service extends Thread {

    public Service() {
        //
    }

    @Override
    public void run() {
        ConnectionManager connectionManager = new ConnectionManager();
        connectionManager.start();
        System.out.println("Connection Manager started!");

        // start core
        DataCore dataCore = new DataCore(connectionManager);
        dataCore.start();
        System.out.println("Core started!");
    }
}
