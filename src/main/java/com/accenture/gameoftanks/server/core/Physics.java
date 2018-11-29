package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Intent;
import com.accenture.gameoftanks.core.Position;
import com.accenture.gameoftanks.core.Tank;

import java.util.List;

import static java.lang.Math.*;

public class Physics {

    // arguments for colision: Position
    private static final float ANGLE_INCREMENT = .02f;

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
        Position pos = tank.getPosition();
        Intent intent = tank.getIntent();

        float velocity = (float) sqrt(pos.vx * pos.vx + pos.vy * pos.vy);
        float thrust = 0.0f;

        if (intent.onForward) {
            thrust = tank.getThrust();
        } else if (intent.onBackWard) {
            thrust = -tank.getThrust();
        }

        if (intent.onTurnLeft) {
            pos.alpha += ANGLE_INCREMENT;
        } else if (intent.onTurnRight) {
            pos.alpha -= ANGLE_INCREMENT;
        }

        // recalculate velocity components
        if (velocity != 0.0f) {
            pos.vx = velocity * (float) cos(pos.alpha);
            pos.vy = velocity * (float) sin(pos.alpha);
        }

        pos.posX += step * pos.vx;
        pos.vx += (step / tank.getMass()) * thrust * cos(pos.alpha);

        pos.posY += step * pos.vy;
        pos.vy += (step / tank.getMass()) * thrust * sin(pos.alpha);
    }

    private static float computeThust(Tank tank) {
        return 0.0f;
    }
}
