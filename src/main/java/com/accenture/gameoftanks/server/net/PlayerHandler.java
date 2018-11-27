package com.accenture.gameoftanks.server.net;

import com.accenture.gameoftanks.core.Player;

import java.io.DataInputStream;
import java.net.Socket;

public class PlayerHandler implements Runnable {

    public Socket socket;
    private DataInputStream streamIn;
    private Player player;

    public PlayerHandler(Socket socket) {
        this.socket = socket;
//        this.player =
    }

    public void run() {
    }

//    Object object = read.object
//    if (object = instanceof data)
//    Data data = (Data)object;

}
