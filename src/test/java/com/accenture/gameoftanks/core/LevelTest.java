package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Shape;
import org.junit.Test;

import static org.junit.Assert.*;

public class LevelTest {

    @Test
    public void getExtends() {
       Level level = new Level();
        float [] extents = level.getExtents();
        float [] expected = new float[] {0.0f, 100.0f, 0.0f, 100.0f};
        assertArrayEquals("Wrong extents", expected, extents, 1.0e-6f);
    }
}