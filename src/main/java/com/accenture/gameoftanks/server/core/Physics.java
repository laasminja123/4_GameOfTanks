package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.*;
import com.accenture.gameoftanks.core.primitives.Edge;
import com.accenture.gameoftanks.core.primitives.Vertex;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.*;
import static com.accenture.gameoftanks.core.MATH.*;

class Physics {

    // arguments for collision: Position
    private static final float EPSILON = 1.0e-5f;
    private static final float PI = (float) Math.PI;

    /**
     * @param entity01 first entity
     * @param entity02 second entity
     */
    static void computeCollision(Entity entity01, Entity entity02) {
        if (entity01 == entity02) {
            return;
        } else if (entity01.isBreakable() && !entity01.isAlive() || entity02.isBreakable() && !entity02.isAlive()) {
            return;
        }
        Vertex [] topology01 = entity01.getTopology();
        Position pos01 = entity01.getPosition();
        transform2d(pos01, topology01);

        Vertex [] topology02 = entity02.getTopology();
        Position pos02 = entity02.getPosition();
        transform2d(pos02, topology02);

        Entity entity1, entity2;
        Vertex [] topology1;
        Vertex [] topology2;
        Position pos1;
        Position pos2;

        Edge edge = new Edge(null, null);
        Vertex v0 = new Vertex(0.0f, 0.0f);
        Edge motionLine = new Edge(v0, null);
        float d;

        for (int n = 1; n <= 2; n++) {
            // set active and passive entities
            if (n == 1) {
                entity1 = entity01;
                entity2 = entity02;

                topology1 = topology01;
                topology2 = topology02;

                pos1 = pos01;
                pos2 = pos02;
            } else {
                entity1 = entity02;
                entity2 = entity01;

                topology1 = topology02;
                topology2 = topology01;

                pos1 = pos02;
                pos2 = pos01;
            }
            v0.x = v0.xt = pos1.posX;
            v0.y = v0.yt = pos1.posY;

            if (!entity1.hasConvexTopology()) {
                continue;
            }

            for (Vertex v : topology1) {
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
                        float[] normal = new float[2];
                        getNormal(normal, edge);

                        float[] normalTranslate = new float[2];
                        System.arraycopy(normal, 0, normalTranslate, 0, 2);

                        scale(d, normalTranslate);

                        // translate active entity back to the edge
                        if (!entity1.isStatic()) {
                            pos1.posX += normalTranslate[0];
                            pos1.posY += normalTranslate[1];
                        }

                        // recompute velocity components ----------------------------------------------

                        // compute bi-normal (it's coincident with edge2)
                        float[] biNormal = new float[2];
                        System.arraycopy(normal, 0, biNormal, 0, 2);
                        rotate(-PI / 2.0f, biNormal);

                        // first body current velocity
                        float[] velocity1 = new float[]{pos1.vx, pos1.vy};

                        // first body tangent velocity
                        float V11t = dot(velocity1, biNormal);  // velocity projection onto biNormal
                        float[] velocity1t = new float[2];
                        System.arraycopy(biNormal, 0, velocity1t, 0, 2);
                        scale(V11t, velocity1t);

                        // first body normal velocity
                        float V11n = dot(normal, velocity1);

                        if (entity2.isStatic()) {
                            if (!entity1.isStatic()) {
                                pos1.vx = velocity1t[0];
                                pos1.vy = velocity1t[1];

                                if (V11n < -0.02f) {
                                    // apply angular momentum caused by impact
                                    float d0 = dot(biNormal, pos1.posX, pos1.posY);
                                    d = dot(biNormal, v) - d0;
                                    pos1.omega += .2 * d * abs(V11n);
                                }
                            }
                        } else {
                            // second body current velocity
                            float[] velocity2 = new float[]{pos2.vx, pos2.vy};

                            // second body tangent velocity
                            float V21t = dot(velocity2, biNormal);  // velocity projection onto biNormal
                            float[] velocity2t = new float[2];
                            System.arraycopy(biNormal, 0, velocity2t, 0, 2);
                            scale(V21t, velocity2t);

                            // second body normal velocity
                            float V21n = dot(normal, velocity2);

                            // resulting normal velocity Magnitude after impact considering absolutely non-elastic interaction
                            float m1 = entity1.getMass();
                            float m2 = entity2.getMass();
                            float Vn = (m1 * V11n + m2 * V21n) / (m1 + m2);

                            // resulting normal velocity full vector
                            float[] velocity0n = new float[2];
                            System.arraycopy(normal, 0, velocity0n, 0, 2);
                            scale(Vn, velocity0n);

                            // resulting velocities
                            add(velocity1t, velocity0n);
                            add(velocity2t, velocity0n);

                            if (!entity1.isStatic()) {
                                pos1.vx = velocity1t[0];
                                pos1.vy = velocity1t[1];
                            }

                            pos2.vx = velocity2t[0];
                            pos2.vy = velocity2t[1];

                            // TODO apply angular momentum
                        }
                    }
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

        if (!vehicle.isAlive()) {
            pos.vx = .0f;
            pos.vy = .0f;
            return;
        }
        float mass = vehicle.getMass();
        float momentOfInertia = vehicle.getMomentOfInertia();
        float torqueXY = vehicle.getTorqueXY();
        float maxOmega = vehicle.getMaxOmega();

        float tFrictionK = 4.0f;
        //float lFrictionK = 1.0f;

        float velocity = (float) sqrt(pos.vx * pos.vx + pos.vy * pos.vy);
        float thrust = computeThrust(vehicle, velocity);

        // compute rotation in plane --------------------------------------------------------------
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

        // compute transverse force ---------------------------------------------------------------
        float transverseForceMag = 0.0f;
        float angle = 0.0f;

        // velocity normalized vector
        float [] vDir = new float[2];
        normalize(vDir, pos.vx, pos.vy);

        // direction normalized vector
        float [] dir = new float[] {(float) cos(pos.alpha), (float) sin(pos.alpha)};

        if (abs(velocity) > 0.01f) {
            float phi = (float) acos(dot(vDir, dir));

            if (abs(phi) > EPSILON && abs(PI - phi) > EPSILON) {
                float direction = cross2D(vDir, dir);

                transverseForceMag = mass * tFrictionK * (float) sin(phi);
                angle = (direction > 0.0f) ? pos.alpha + PI / 2.0f : pos.alpha - PI / 2.0f;
            }
        }

        // compute longitudinal friction force ----------------------------------------------------
        float longFriction = .0f;

        /*
        if (abs(velocity) > 0.01f) {
            float longComp = dot(vDir, dir);
            float tolerance = .5f;

            if (abs(longComp) >= tolerance) {
                // usual case
                longFriction = -mass * lFrictionK * signum(longComp);
            } else {
                // linear interpolation in range 0..tolerance to make soft braking near zero
                float K = mass * lFrictionK / tolerance;
                longFriction = -K * longComp;
            }
        }
        */

        // compute translation along X axis
        pos.posX += step * pos.vx;
        float forceX = (thrust * (float) cos(pos.alpha) +
                longFriction * (float) cos(pos.alpha) +
                transverseForceMag * (float) cos(angle));
        pos.vx += (step / mass) * forceX;

        // compute translation along Y axis
        pos.posY += step * pos.vy;
        float forceY = (thrust * (float) sin(pos.alpha) +
                longFriction * (float) sin(pos.alpha) +
                transverseForceMag * (float) sin(angle));
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

    static void adjustGunDirection(Vehicle vehicle) {
        Position pos = vehicle.getPosition();
        Intent intent = vehicle.getIntent();
        float angleIncrement = .025f;

        if (intent.onAdjustShootingAngle) {
            float [] direction = new float[2];
            direction[0] = (float) cos(pos.alpha);
            direction[1] = (float) sin(pos.alpha);

            float gunAngle = vehicle.getShootingAngle();
            rotate(gunAngle, direction);  // now direction stores absolute gun angle

            // compute requested gun direction
            float [] yaw = new float[2];
            yaw[0] = (float) cos(intent.shootingAngle);
            yaw[1] = (float) sin(intent.shootingAngle);

            float cosA = dot(yaw, direction);
            float delta = (float) acos(cosA);

            if (1 + cosA > EPSILON) {  // gun NOT directed opposite to requested fire line
                angleIncrement = min(angleIncrement, abs(delta));
                float side = cross2D(direction, yaw);
                angleIncrement *= signum(side);
            }
            vehicle.incrementShootingAngle(angleIncrement);
        }
    }

    static void processShootRequest(DataCore core, Vehicle vehicle, int step) {
        if (!vehicle.isAlive()) {
            return;
        }
        Intent intent = vehicle.getIntent();

        if (intent.onShoot) {
            if (vehicle.getCurrentDelayMsec() == 0) {
                // process shooting
                float mass = vehicle.getBulletMass();
                float power = vehicle.getBulletPower();
                float velocity = vehicle.getBulletVelocity();
                Bullet bullet = new Bullet(vehicle.getId(), mass, power, velocity);

                // setup bullet params
                Position position = vehicle.getPosition();
                bullet.angle = position.alpha + vehicle.getShootingAngle();
                bullet.posX = position.posX;
                bullet.posY = position.posY;
                vehicle.resetShootingDelay();

                // add stats to the player
                Player attacker = core.getPlayer(vehicle.getId());

                if (attacker != null) {
                    attacker.incrementShoots();
                }

                // add this bullet to event list
                core.createBullet(bullet);
            } else {
                vehicle.incrementShootingDelay(step);
            }
        } else {
            vehicle.incrementShootingDelay(step);
        }
    }

    static void computeBulletMotion(DataCore core,
                                    Bullet bullet,
                                    List<Vehicle> vehicles,
                                    List<Entity> entities,
                                    float step) {
        // update position
        float [] startPos = new float[] {bullet.posX, bullet.posY};
        float V = bullet.getVelocity();
        float [] displacement = new float[2];
        displacement[0] = (float) cos(bullet.angle) * V * step;
        displacement[1] = (float) sin(bullet.angle) * V * step;

        bullet.posX += displacement[0];
        bullet.posY += displacement[1];

        // check collisions with existing vehicles and static objects
        List<Entity> targets = new LinkedList<>();
        targets.addAll(vehicles);
        targets.addAll(entities);
        computeBulletCollision(core, bullet, targets, startPos, displacement);
    }

    private static void computeBulletCollision(DataCore core,
                                               Bullet bullet,
                                               List<Entity> entities,
                                               float [] startPos,
                                               float [] displacement) {
        Vertex v1 = new Vertex(startPos[0], startPos[1]);
        Vertex v2 = new Vertex(startPos[0] + displacement[0], startPos[1] + displacement[1]);
        Edge fireLine = new Edge(v1, v2);

        for (Entity entity: entities) {
            // check whether this is NOT a parent vehicle
            if (entity instanceof Vehicle && entity.getId() == bullet.getVehicleId()) {
                continue;
            } else if (entity instanceof Building && entity.isBreakable() && !entity.isAlive()) {
                continue;
            }
            Vertex [] topology = entity.getTopology();
            Position pos = entity.getPosition();
            transform2d(pos, topology);

            Edge edge = new Edge(null, null);

            for (int i = 0; i < topology.length; i++) {
                // create edge
                if (i < topology.length - 1) {
                    edge.v1 = topology[i];
                    edge.v2 = topology[i + 1];
                } else {
                    edge.v1 = topology[i];
                    edge.v2 = topology[0];
                }

                // check intersection
                if (intersects(fireLine, edge) > 0.0f) {
                    // collision detected
                    if (entity instanceof Vehicle) {
                        Vehicle vehicle = (Vehicle) entity;
                        vehicle.decreaseCurrentHp((int) bullet.getPower());

                        // add scores to attacker player
                        Player attacker = core.getPlayer(bullet.getVehicleId());

                        if (attacker != null) {
                            attacker.incrementHits();

                            if (!vehicle.isAlive()) {
                                Player target = core.getPlayer(vehicle.getId());

                                if (target != null) {
                                    attacker.incrementKills();
                                    target.incrementDeaths();
                                }
                            }
                        }
                    } else if (entity.isBreakable()) {
                        if (!entity.isAlive()) {
                            return;
                        }
                        entity.decreaseCurrentHp((int) bullet.getPower());

                        if (!entity.isAlive()) {
                            core.addDeadObject(entity.getId());
                        }
                    }
                    bullet.isConsumed = true;
                    core.consumeBullet(bullet);
                    return;
                }
            }
        }
    }
}
