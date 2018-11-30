package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.*;
import com.accenture.gameoftanks.core.primitives.Edge;
import com.accenture.gameoftanks.core.primitives.Vertex;

import static java.lang.Math.*;
import static com.accenture.gameoftanks.core.MATH.*;

class Physics {

    // arguments for collision: Position
    private static final float ANGLE_INCREMENT = .02f;
    private static final float EPSYLON = 1.0e-2f;


    /**
     *
     * @param entity1 moving entity
     * @param entity2 passive entity
     * @param step: integration time step
     */
    static void computeCollision(Entity entity1, Entity entity2, float step) {
        Vertex [] topology1 = entity1.getTopology();
        Position pos1 = entity1.getPosition();
        transform2d(pos1, topology1);

        Vertex [] topology2 = entity2.getTopology();
        Position pos2 = entity2.getPosition();
        transform2d(pos2, topology2);

        Edge edge = new Edge(null, null);
        Vertex v0 = new Vertex(0.0f, 0.0f);
        Edge motionLine = new Edge(v0, null);

        float d;

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
                if ((d = intersects(motionLine, edge)) > 0.0f) {  // collision detected
                    // create back translation vector
                    float [] unitDir = new float[2];
                    normalize(unitDir, motionLine);
                    reverse(unitDir);

                    float [] normal = new float[2];
                    getNormal(normal, edge);

                    float cosA = dot(unitDir, normal);
                    d /= cosA;  // translation magnitude
                    scale(d + EPSYLON, unitDir);

                    // translate positions
                    pos1.posX += unitDir[0];
                    pos1.posY += unitDir[1];

                    // recompute speed components
                    float [] biNormal = new float[2];
                    normalize(biNormal, edge);

                    float V2abs = dot(pos1.vx, pos1.vy, biNormal[0], biNormal[1]);
                    scale(V2abs, biNormal);  // now biNormal is new velocity vector

                    pos1.vx = biNormal[0];
                    pos1.vy = biNormal[1];

                    System.out.println("Collision detected");
                }
            }
        }
    }

    static void computeMotion(Vehicle vehicle, float step) {
        Position pos = vehicle.getPosition();
        Intent intent = vehicle.getIntent();
        float mass = vehicle.getMass();

        float frictionK = 3.0f; // TODO implement it for vehicle

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

        // compute transverse force
        float transverseForceMag = 0.0f;
        float angle = 0.0f;

        if (velocity != 0.0f) {
            // velocity normalized vector
            float [] vDir = new float[2];
            normalize(vDir, pos.vx, pos.vy);

            // direction normalized vector
            float [] dir = new float[2];
            dir[0] = (float) cos(pos.alpha);
            dir[1] = (float) sin(pos.alpha);

            float direction = cross2D(vDir, dir);
            float phi = (float) acos(dot(vDir, dir));

            System.out.println("direction is : " + direction);

            transverseForceMag = mass * frictionK * (float) sin(phi);
            angle = (direction > 0.0f) ? pos.alpha + (float) PI / 2.0f : pos.alpha - (float) PI / 2.0f;
        }

        pos.posX += step * pos.vx;
        float forceX = (thrust * (float) cos(pos.alpha) + transverseForceMag * (float) cos(angle));
        pos.vx += (step / mass) * forceX;

        pos.posY += step * pos.vy;
        float forceY = (thrust * (float) sin(pos.alpha) + transverseForceMag * (float) sin(angle));
        pos.vy += (step / mass) * forceY;

        //System.out.println("X is : " + pos.posX);
        //System.out.println("Y is : " + pos.posY);
    }

    private static float computeThust(Tank tank) {
        return 0.0f;
    }
}
