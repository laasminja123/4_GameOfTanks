package com.accenture.gameoftanks.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class LevelTest {

    @Test
    public void computeColision() {
        Tank tank = new Tank(1, 1, 1);
        Position position = tank.getPosition();
        Level level = new Level(0, 10, 10, 0);
        float delta = 1.e-7f;
        level.computeColision(tank);
        if (position.posX + tank.length / 2 > level.rightBoundary) {
            assertEquals("Tank is out of right boundary and isn't returned back", level.rightBoundary - tank.length / 2, position.posX, delta);
        }
        if (position.posX - tank.length / 2 < level.leftBoundary) {
            assertEquals("Tank is out of left boundary and isn't returned back", level.leftBoundary + tank.length / 2, position.posX, delta);
        }
        if (position.posY + tank.length / 2 > level.topBoundary) {
            assertEquals("Tank is out of top boundary and isn't returned back", level.topBoundary - tank.length / 2, position.posY, delta);
        }
        if (position.posY - tank.length / 2 < level.bottomBoundary) {
            assertEquals("Tank is out of bottom boundary and isn't returned back", level.bottomBoundary + tank.length / 2, position.posY, delta);
        }

    }
}