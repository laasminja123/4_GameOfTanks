package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.*;
import com.accenture.gameoftanks.server.net.ConnectionManager;

import java.util.LinkedList;
import java.util.List;

public class DataCore extends Thread {

    private static final int TIME_STEP_MSEC = 40;
    private static float TIME_STEP_FLOAT;

    private ConnectionManager connectionManager;

    private boolean onDemand;

    DataCore(ConnectionManager connectionManager) {
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

            Level level = connectionManager.getLevel();

            if (level == null) {
                continue;
            }

            // collect vehicles from all players --------------------------------------------------
            List<Player> players = connectionManager.getPlayers();
            List<Vehicle> vehicles = new LinkedList<>();

            for (Player player: players) {
                Vehicle vehicle = player.getVehicle();

                if (vehicle != null) {
                    vehicles.add(vehicle);
                }
            }

            // compute collisions with level static objects ---------------------------------------
            List<Entity> levelContent = level.getContent();

            for (Vehicle vehicle: vehicles) {
                for (Entity entity: levelContent) {
                    Physics.computeCollision(vehicle, entity);
                }
            }

            // compute collisions with other movable objects --------------------------------------
            int numVehicles = vehicles.size();

            for (int i = 0; i < numVehicles - 1; i++) {
                Vehicle activeVehicle = vehicles.get(i);

                for (int j = i + 1; j < numVehicles; j++) {
                    Vehicle passiveVehicle = vehicles.get(j);
                    Physics.computeCollision(activeVehicle, passiveVehicle);
                }
            }

            // compute motion ---------------------------------------------------------------------
            for (Vehicle vehicle: vehicles) {
                Physics.computeMotion(vehicle, TIME_STEP_FLOAT);
            }
        }
    }
}
