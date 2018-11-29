package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Level;
import com.accenture.gameoftanks.server.net.ConnectionManager;

public class Service extends Thread {

    public Service() {
        //
    }

    @Override
    public void run() {
        // create game level
        Level gameLevel = new Level(0.0f, 100.0f, 100.0f, 0.0f);

        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.start();

        ConnectionManager connectionManager = new ConnectionManager(gameLevel, databaseManager);
        connectionManager.start();
        System.out.println("Connection Manager started!");

        // start core
        DataCore dataCore = new DataCore(connectionManager);
        dataCore.start();
        System.out.println("Core started!");
    }
}
