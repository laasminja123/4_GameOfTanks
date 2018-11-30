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
        Level gameLevel = new Level(0.0f, 100.0f, 100.0f, 0.0f);

        createDatabaseManager();

        ConnectionManager connectionManager = new ConnectionManager(gameLevel, databaseManager);
        connectionManager.start();
        System.out.println("Connection Manager started!");

        // start core
        DataCore dataCore = new DataCore(connectionManager);
        dataCore.start();
        System.out.println("Core started!");
    }

    public void createDatabaseManager() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you have and wish to use MySQL database? Answer no if you do not know what it is [y n]");
        String string;
        while (true) {
            string = scanner.nextLine();
            if (string.equals("y") | string.equals("Y") | string.equals("yes") | string.equals("Yes")) {
                System.out.println("Enter your MySQL username");
                String username = scanner.nextLine();
                System.out.println("Enter your MySQL password");
                String password = scanner.nextLine();
                databaseManager = new DatabaseManager(username, password);
                return;
            } else if (string.equals("n") | string.equals("N") | string.equals("no") | string.equals("No")) {
                databaseManager = null;
                return;
            }
        }
    }
}