package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.*;
import com.accenture.gameoftanks.core.primitives.Edge;
import com.accenture.gameoftanks.core.primitives.Vertex;

import static java.lang.Math.*;
import static com.accenture.gameoftanks.core.MATH.*;

public class Physics {

    // arguments for collision: Position
    private static final float ANGLE_INCREMENT = .02f;

    /**
     *
     * @param entity1 moving entity
     * @param entity2 passive entity
     * @param step: integration time step
     */
    public void computeCollision(Entity entity1, Entity entity2, float step) {
        Vertex [] topology1 = entity1.getTopology();
        Position pos1 = entity1.getPosition();
        transform2d(pos1, topology1);

        Vertex [] topology2 = entity2.getTopology();
        Position pos2 = entity2.getPosition();
        transform2d(pos2, topology2);

        Edge edge = new Edge(null, null);
        Vertex v0 = new Vertex(0.0f, 0.0f);
        Edge motionLine = new Edge(v0, null);

        for (Vertex v: topology1) {
            for (int i = 0; i < topology2.length; i++) {
                // create edge
                if (i < topology2.length - 1) {
                    edge.v1 = topology2[i];
                    edge.v2 = topology2[i + 1];
                } else {
                    edge.v1 = topology2[i];
                    edge.v2 = topology2[0];
                }

                // compute motion line
                v0.xt = v.xt - pos1.vx * step;
                v0.yt = v.yt - pos1.vy * step;
                motionLine.v2 = v;

                // check intersection
                //
            }
        }
    }

    public static void computeMotion(Vehicle vehicle, float step) {
        Position pos = vehicle.getPosition();
        Intent intent = vehicle.getIntent();

        float velocity = (float) sqrt(pos.vx * pos.vx + pos.vy * pos.vy);
        float thrust = 0.0f;

        if (intent.onForward) {
            thrust = vehicle.getThrust();
        } else if (intent.onBackWard) {
            thrust = -vehicle.getThrust();
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

        float mass = vehicle.getMass();
        pos.posX += step * pos.vx;
        pos.vx += (step / mass) * thrust * cos(pos.alpha);

        pos.posY += step * pos.vy;
        pos.vy += (step / mass) * thrust * sin(pos.alpha);
    }

    private static float computeThust(Tank tank) {
        return 0.0f;
    }
}
