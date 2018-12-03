package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.*;
import com.accenture.gameoftanks.core.primitives.Edge;
import com.accenture.gameoftanks.core.primitives.Vertex;

import static java.lang.Math.*;
import static com.accenture.gameoftanks.core.MATH.*;

class Physics {

    // arguments for collision: Position
    private static final float EPSILON = 1.0e-5f;
    private static final float PI = (float) Math.PI;

    /**
     * @param entity1 active entity (it should be always movable entity)
     * @param entity2 passive entity (it can be either movable or static)
     */
    static void computeCollision(Entity entity1, Entity entity2) {
        if (entity1 == entity2) {
            return;
        }
        Vertex [] topology1 = entity1.getTopology();
        Position pos1 = entity1.getPosition();
        transform2d(pos1, topology1);

        Vertex [] topology2 = entity2.getTopology();
        Position pos2 = entity2.getPosition();
        transform2d(pos2, topology2);

        Edge edge = new Edge(null, null);
        Vertex v0 = new Vertex(pos1.posX, pos1.posY);
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
                motionLine.v2 = v;

                // check intersection
                if ((d = intersects(motionLine, edge)) > 0.0f) {
                    // compute new entity position ------------------------------------------------
                    float [] normal = new float[2];
                    getNormal(normal, edge);

                    float [] normalTranslate = new float[2];
                    System.arraycopy(normal, 0, normalTranslate, 0, 2);

                    scale(d, normalTranslate);

                    // translate active entity back to the edge
                    pos1.posX += normalTranslate[0];
                    pos1.posY += normalTranslate[1];

                    // recompute velocity components ----------------------------------------------

                    // compute bi-normal (it's coincident with edge2)
                    float [] biNormal = new float[2];
                    System.arraycopy(normal, 0, biNormal, 0, 2);
                    rotate(-PI / 2.0f, biNormal);

                    // first body current velocity
                    float [] velocity1 = new float[] {pos1.vx, pos1.vy};

                    // first body tangent velocity
                    float V11t = dot(velocity1, biNormal);  // velocity projection onto biNormal
                    float [] velocity1t = new float[2];
                    System.arraycopy(biNormal, 0, velocity1t, 0, 2);
                    scale(V11t, velocity1t);

                    // first body normal velocity
                    float V11n = dot(normal, velocity1);

                    if (entity2.isStatic()) {
                        pos1.vx = velocity1t[0];
                        pos1.vy = velocity1t[1];

                        if (V11n < -0.02f) {
                            // apply angular momentum caused by impact
                            float d0 = dot(biNormal, pos1.posX, pos1.posY);
                            d = dot(biNormal, v) - d0;
                            pos1.omega += .2 * d * abs(V11n);
                        }
                    } else {
                        // second body current velocity
                        float [] velocity2 = new float[] {pos2.vx, pos2.vy};

                        // second body tangent velocity
                        float V21t = dot(velocity2, biNormal);  // velocity projection onto biNormal
                        float [] velocity2t = new float[2];
                        System.arraycopy(biNormal, 0, velocity2t, 0, 2);
                        scale(V21t, velocity2t);

                        // second body normal velocity
                        float V21n = dot(normal, velocity2);

                        // resulting normal velocity Magnitude after impact considering absolutely non-elastic interaction
                        float m1 = entity1.getMass();
                        float m2 = entity2.getMass();
                        float Vn = (m1 * V11n + m2 * V21n) / (m1 + m2);

                        // resulting normal velocity full vector
                        float [] velocity0n = new float[2];
                        System.arraycopy(normal, 0, velocity0n, 0, 2);
                        scale(Vn, velocity0n);

                        // resulting velocities
                        add(velocity1t, velocity0n);
                        add(velocity2t, velocity0n);

                        pos1.vx = velocity1t[0];
                        pos1.vy = velocity1t[1];

                        pos2.vx = velocity2t[0];
                        pos2.vy = velocity2t[1];

                        // TODO apply angular momentum
                    }
                    System.out.println("Collision detected");
                }
            }
        }
    }

    /**
     * Body position is calculated based on Newton's 2-nd law.
     * Diff equations are integrated with Euler's first order method
     *
     * @param vehicle - active vehicle on battleground
     * @param step - integration step (seconds)
     */
    static void computeMotion(Vehicle vehicle, float step) {
        Position pos = vehicle.getPosition();
        Intent intent = vehicle.getIntent();
        float mass = vehicle.getMass();
        float momentOfInertia = vehicle.getMomentOfInertia();
        float torqueXY = vehicle.getTorqueXY();
        float maxOmega = vehicle.getMaxOmega();

        float tFrictionK = 4.0f; // TODO implement it for vehicle

        float velocity = (float) sqrt(pos.vx * pos.vx + pos.vy * pos.vy);
        float thrust = computeThrust(vehicle, velocity);

        // compute rotation in plane
        float torque = 0.0f;

        if (intent.onTurnLeft) {
            torque = torqueXY;
        } else if (intent.onTurnRight) {
            torque = -torqueXY;
        }

        if (abs(pos.omega) > maxOmega) {
            torque = 0.0f;
        }

        float rFriction = 0.0f;

        if (torque == 0.0f && abs(pos.omega) > EPSILON) {
            rFriction = -4000.0f * signum(pos.omega);
        }

        pos.alpha += step * pos.omega;
        pos.omega += (step / momentOfInertia) * (torque + rFriction);

        // compute transverse force
        float transverseForceMag = 0.0f;
        float angle = 0.0f;

        if (abs(velocity) > 0.01f) {
            // velocity normalized vector
            float [] vDir = new float[2];
            normalize(vDir, pos.vx, pos.vy);

            // direction normalized vector
            float [] dir = new float[2];
            dir[0] = (float) cos(pos.alpha);
            dir[1] = (float) sin(pos.alpha);

            float phi = (float) acos(dot(vDir, dir));

            if (abs(phi) > EPSILON && abs(PI - phi) > EPSILON) {
                float direction = cross2D(vDir, dir);

                transverseForceMag = mass * tFrictionK * (float) sin(phi);
                angle = (direction > 0.0f) ? pos.alpha + PI / 2.0f : pos.alpha - PI / 2.0f;
            }
        }

        // compute translation along X axis
        pos.posX += step * pos.vx;
        float forceX = (thrust * (float) cos(pos.alpha) + transverseForceMag * (float) cos(angle));
        pos.vx += (step / mass) * forceX;

        // compute translation along Y axis
        pos.posY += step * pos.vy;
        float forceY = (thrust * (float) sin(pos.alpha) + transverseForceMag * (float) sin(angle));
        pos.vy += (step / mass) * forceY;
    }

    private static float computeThrust(Vehicle vehicle, float velocity) {
        Intent intent = vehicle.getIntent();
        float thrust = 0.0f;

        if (intent.onForward) {
            thrust = vehicle.getThrust();
        } else if (intent.onBackWard) {
            thrust = -vehicle.getThrust();
        }

        if (velocity >= vehicle.getMaxSpeed()) {
            thrust = 0.0f;
        }
        return thrust;
    }
}
