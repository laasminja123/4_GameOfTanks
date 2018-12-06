package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.server.net.ConnectionManager;

import java.util.Scanner;

public class CommandListener extends Thread {

    private DatabaseManager databaseManager;
    private Scanner scanner;

    public CommandListener(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void run() {
        while (true) {

            System.out.println();

            if (databaseManager != null) {
                System.out.println("Type command [logs] for logs.");
                System.out.println("Type command [top10] for 10 highest scored players");
            }

            System.out.println("Type command [exit] to close server and exit.");
            System.out.println();

            String command = scanner.nextLine();

            if (command.equals("exit")) {
                if (databaseManager != null) {
                    databaseManager.closeConnection();
                }
                break;
            }

            if (databaseManager != null) {

                if (command.equals("logs")) {
                    databaseManager.printLogs();
                }

                if (command.equals("top10")) {
                    databaseManager.printTopPlayers();
                }
            }


        }
    }
}
