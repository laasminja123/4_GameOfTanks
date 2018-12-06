package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Vertex;
import org.junit.Test;

import static org.junit.Assert.*;

public class TankTest {

    @Test
    public void createTopology() {
        float length = 4;
        float width = 3;

        Tank tank1 = new Tank(length, width);
        Vertex[] tp = tank1.getTopology();


        assertEquals("Wrong vertex coordinate, expected " + (-length / 2.0f), -length / 2.0f, tp[0].x, 1.0e-6f);
        assertEquals("Wrong vertex coordinate, expected " + (-width / 2.0f), -width / 2.0f, tp[0].y, 1.0e-6f);
    }
}