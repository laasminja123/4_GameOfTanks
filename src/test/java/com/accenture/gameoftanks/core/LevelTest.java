package com.accenture.gameoftanks.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class LevelTest {

    @Test
    public void computeColision() {
        Tank tank = new Tank(1, 1);
        Position position = tank.getPosition();
        Level level = new Level(0, 10, 10, 0);
        level.computeColision(tank);

    }
}