package com.accenture.gameoftanks.server.core;

import com.accenture.gameoftanks.core.Entity;
import com.accenture.gameoftanks.core.Position;
import com.accenture.gameoftanks.core.Tank;
import org.junit.Test;

import static org.junit.Assert.*;

public class PhysicsTest {

    @Test
    public void computeCollision() {
        float delta = .5f;
        float length = 4.0f;

        Tank tank1 = new Tank(length, 2.0f);
        Position position1 = tank1.getPosition();

        Tank tank2 = new Tank(length, 2.0f);
        Position position2 = tank2.getPosition();
        position2.posX = length - delta;

        Physics.computeCollision(tank1, tank2);

        assertEquals("Wrong value", -delta, position1.posX, 1.0e-6f);
    }
}