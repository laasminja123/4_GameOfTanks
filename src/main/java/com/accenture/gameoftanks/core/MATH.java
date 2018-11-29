package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Vertex;

import static java.lang.Math.*;

public class MATH {

    private MATH(){}

    public static void rotate2d(float angle, Vertex... vertices) {
        for (Vertex vertex: vertices) {
            float x2 = (float) (vertex.x * cos(angle) + vertex.y * -sin(angle));
            float y2 = (float) (vertex.x * sin(angle) + vertex.y *  cos(angle));
            vertex.x = x2;
            vertex.y = y2;
        }
    }
}
