package com.accenture.gameoftanks.server;

import com.accenture.gameoftanks.server.core.Service;

public class ServerSession {
    public static void main(String [] args) {
        Service service = new Service();
        service.start();
        System.out.println("Server for Game of Tanks started!");
    }
}
