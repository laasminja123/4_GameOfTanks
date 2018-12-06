package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Level;
import com.accenture.gameoftanks.server.net.ConnectionManager;

import java.util.Scanner;

public class Service extends Thread {

    DatabaseManager databaseManager;

    public Service() {
        //
    }

    @Override
    public void run() {
        // create game level
        Level gameLevel = new Level();

        useDatabaseManager(true);

        ConnectionManager connectionManager = new ConnectionManager(gameLevel, databaseManager);
        connectionManager.start();
        System.out.println("Connection Manager started!");

        // start core
        DataCore dataCore = new DataCore(connectionManager);
        connectionManager.setDataCore(dataCore);
        dataCore.start();


        if (databaseManager != null) {
            System.out.println("Core started!");
            CommandListener commandListener = new CommandListener(databaseManager);
            commandListener.start();
        } else {
            System.out.println("Core started!");
        }

        try {
            connectionManager.join();
            dataCore.join();
        } catch (InterruptedException exc) {
            exc.printStackTrace();
        } finally {
            databaseManager.closeConnection();
        }
    }

    public void createDatabaseManager() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you have and wish to use MySQL database? Answer no if you do not know what it is [y n]");
        String string;
        while (true) {
            string = scanner.nextLine();
            if (string.equals("y") || string.equals("Y") || string.equals("yes") || string.equals("Yes")) {
                System.out.println("Enter your MySQL username");
                String username = scanner.nextLine();
                System.out.println("Enter your MySQL password");
                String password = scanner.nextLine();
                databaseManager = new DatabaseManager(username, password);
                return;
            } else if (string.equals("n") || string.equals("N") || string.equals("no") || string.equals("No")) {
                databaseManager = null;
                return;
            }
        }
    }

    public void useDatabaseManager(boolean value) {
        if (value) {
            createDatabaseManager();
        } else {
            databaseManager = null;
        }
    }
}