package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Edge;
import com.accenture.gameoftanks.core.primitives.Vertex;
import org.junit.Test;

import static org.junit.Assert.*;

public class MATHTest {

    private static final float DELTA = 1.0e-6f;

    @Test
    public void getNormal() {
        Edge testEdge = new Edge(new Vertex(0, 0), new Vertex(10, 0));
        float[] normal = new float[2];
        MATH.getNormal(normal, testEdge);
        assertEquals("Error: expected X is 0", 0.0f, normal[0], DELTA);
        assertEquals("Error: expected y is 1", 1.0f, normal[1], DELTA);
    }

    @Test
    public void normalize() {
        float vx = 100;
        float vy = 0;
        float[] direction = new float[2];
        MATH.normalize(direction, vx, vy);
        assertEquals("Error: expected X is 1", 1.0f, direction[0], DELTA);
        assertEquals("Error: expected y is 0", 0.0f, direction[1], DELTA);
    }


}