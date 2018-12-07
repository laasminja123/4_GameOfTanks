package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Level;
import com.accenture.gameoftanks.server.net.ConnectionManager;

import java.util.Scanner;

public class Service extends Thread {

    private static final boolean USE_DATABASE_MANAGER = true;
    DatabaseManager databaseManager;

    @Override
    public void run() {
        // create game level
        Level gameLevel = new Level();

        useDatabaseManager(USE_DATABASE_MANAGER);

        ConnectionManager connectionManager = new ConnectionManager(gameLevel, databaseManager);
        connectionManager.start();
        System.out.println("Connection Manager started!");

        // start core
        DataCore dataCore = new DataCore(connectionManager);
        connectionManager.setDataCore(dataCore);
        dataCore.start();
        System.out.println("Core started!");

        CommandListener commandListener = new CommandListener(databaseManager);
        commandListener.run();

        // close service
        connectionManager.stopService();
        dataCore.stopService();

    }

    public void createDatabaseManager() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you have and wish to use MySQL database? Answer no if you do not know what it is [y n]");
        String string;
        while (true) {
            string = scanner.nextLine();
            if (string.equals("y") || string.equals("Y") || string.equals("yes") || string.equals("Yes")) {
                int i = 5;

                while (i > 0) {

                    System.out.println("Enter your MySQL username");
                    String username = scanner.nextLine();
                    System.out.println("Enter your MySQL password");
                    String password = scanner.nextLine();

                    try {
                        databaseManager = new DatabaseManager(username, password);
                        break;
                    } catch (Exception e) {
                        i--;
                        System.out.println("Could not connect to database, possibly username or password is incorrect or you have not setup MySQL properly.");
                        if (i == 0) {
                            System.out.println("You're out of tries, database functionality hsa been disabled, continuing with the creation of server.");
                            System.out.println();
                            break;
                        } else if (i == 1) {
                            System.out.println(i + " try remaining.");
                        } else {
                            System.out.println(i + " tries remaining.");
                        }

                    }

                }
                databaseManager.setupDatabase();
                return;

                } else if (string.equals("n") || string.equals("N") || string.equals("no") || string.equals("No")) {
                    databaseManager = null;
                    return;
                }
            }
        }

        public void useDatabaseManager ( boolean value){
            if (value) {
                createDatabaseManager();
            } else {
                databaseManager = null;
            }
        }
    }