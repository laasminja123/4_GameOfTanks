package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Player;
import com.accenture.gameoftanks.core.Tank;
import com.accenture.gameoftanks.core.Vehicle;
import com.accenture.gameoftanks.server.net.ConnectionManager;

import java.util.List;

public class DataCore extends Thread {

    private static final int TIME_STEP_MSEC = 40;
    private static float TIME_STEP_FLOAT;

    private ConnectionManager connectionManager;

    private boolean onDemand;

    public DataCore(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        TIME_STEP_FLOAT = (float) TIME_STEP_MSEC / (float) 1000;
        onDemand = true;
    }

    @Override
    public void run() {
        while (onDemand) {
            try {
                Thread.sleep(TIME_STEP_MSEC);
            } catch (InterruptedException exc) {
                //
            }

            // collect positions from all players
            List<Player> players = connectionManager.getPlayers();

            for (Player player: players) {
                Vehicle vehicle = player.getVehicle();

                if (vehicle != null) {
                    Physics.computeMotion(vehicle, TIME_STEP_FLOAT);
                }
            }
        }
    }
}
