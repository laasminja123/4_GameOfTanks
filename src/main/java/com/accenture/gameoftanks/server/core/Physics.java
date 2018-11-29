package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Intent;
import com.accenture.gameoftanks.core.Position;
import com.accenture.gameoftanks.core.Tank;

import java.util.List;

import static java.lang.Math.*;

public class Physics {

    // arguments for colision: Position

    public void computeColision() {
        float origin1X = 0.0f;
        float origin1Y = 0.0f;

        float origin2X = 0.0f;
        float origin2Y = 0.0f;

        float size1X = 1.0f;
        float size2X = 1.0f;

        float size1Y = 1.0f;
        float size2Y = 1.0f;

        // TODO
        /*
        if (Math.abs(origin1X - origin1Y) < (size1X / 2.0f + size2X / 2.0f)) {
            if (origin1X)
        }
        */
    }

    public static void computeMotion(Tank tank, float step) {
        Position position = tank.getPosition();
        Intent intent = tank.getIntent();
        float thrust = 0.0f;

        if (intent.isOnMove()) {
            thrust = tank.getThrust();
        }

        position.posX += step * position.vx;
        position.vx += (step / tank.getMass()) * thrust * cos(intent.getMoveAngle());

        position.posY += step * position.vy;
        position.vy += (step / tank.getMass()) * thrust * sin(intent.getMoveAngle());

        System.out.println("OnMove is: " + intent.isOnMove());
    }
}
