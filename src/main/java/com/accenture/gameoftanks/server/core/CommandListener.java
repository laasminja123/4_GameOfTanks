package com.accenture.gameoftanks.server.core;

import java.util.Scanner;

public class CommandListener extends Thread {

    private DatabaseManager databaseManager;
    private Scanner scanner;

    public CommandListener(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void start() {
        while (true) {

            System.out.println();
//            System.out.println("Type command [exit] to exit.");
            System.out.println("Type command [printLogs] to print logs.");
            System.out.println();

            String command = scanner.nextLine();

//            if (command.equals("exit")) {
//                break;
//            }

            if (command.equals("printLogs")) {
                databaseManager.printLogs();
            }

        }
    }
}
