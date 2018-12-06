package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.*;
import com.accenture.gameoftanks.server.net.ConnectionManager;

import java.util.LinkedList;
import java.util.List;

public class DataCore extends Thread {

    private static final int TIME_STEP_MSEC = 40;
    private static float TIME_STEP_FLOAT;

    private ConnectionManager connectionManager;

    private List<Player> players;
    private List<Bullet> bullets;
    private Level level;

    private boolean onDemand;

    DataCore(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        TIME_STEP_FLOAT = (float) TIME_STEP_MSEC / (float) 1000;
        bullets = new LinkedList<>();
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

            level = connectionManager.getLevel();

            if (level == null) {
                continue;
            }
            List<Entity> levelContent = level.getContent();

            // collect vehicles from all players --------------------------------------------------
            players = connectionManager.getPlayers();
            List<Vehicle> vehicles = new LinkedList<>();

            for (Player player : players) {
                Vehicle vehicle = player.getVehicle();

                if (vehicle != null) {
                    vehicles.add(vehicle);
                }
            }

            // process shooting action ------------------------------------------------------------
            for (Vehicle vehicle : vehicles) {
                Physics.processShootRequest(this, vehicle, TIME_STEP_MSEC);
            }

            // compute bullets motion -------------------------------------------------------------
            for (Bullet bullet : bullets) {
                Physics.computeBulletMotion(this, bullet, vehicles, levelContent, TIME_STEP_FLOAT);
            }

            // compute collisions with level static objects ---------------------------------------
            for (Vehicle vehicle : vehicles) {
                for (Entity entity : levelContent) {
                    Physics.computeCollision(vehicle, entity);
                }
            }

            // compute collisions with other movable objects --------------------------------------
            for (Vehicle activeVehicle : vehicles) {
                for (Vehicle passiveVehicle : vehicles) {
                    Physics.computeCollision(activeVehicle, passiveVehicle);
                }
            }

            // compute motion ---------------------------------------------------------------------
            for (Vehicle vehicle : vehicles) {
                Physics.computeMotion(vehicle, TIME_STEP_FLOAT);
            }

            // rotate guns ------------------------------------------------------------------------
            for (Vehicle vehicle : vehicles) {
                Physics.adjustGunDirection(vehicle);
            }
        }
    }

    void createBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    void consumeBullet(Bullet bullet) {
        bullets.remove(bullet);
    }

    void addDeadObject(int id) {
        if (level != null) {
            level.addDeadObject(id);
        }
    }

    Player getPlayer(int vehicleId) {
        if (players != null) {
            for (Player player : players) {
                if (player.getVehicle().getId() == vehicleId) {
                    return player;
                }
            }
        }
        return null;
    }

    public void stopService() {
        onDemand = false;
    }
}
