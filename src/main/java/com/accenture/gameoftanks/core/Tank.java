package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Shape;

import java.io.Serializable;

public class Tank extends Vehicle implements Serializable {

    private final float length;
    private final float width;

    Tank(float length, float width) {
        super(5000.0f, 5000.0f, 10000.0f, 8, 2000.0f, 1.3f, 100);
        Shape topology = new Shape(createTopology(length, width));
        setTopology(topology);

        this.width = width;
        this.length = length;
    }

    private float [] createTopology(float length, float width) {
        float [] vertices = new float[8];

        vertices[0] = -length / 2.0f;
        vertices[1] = -width / 2.0f;

        vertices[2] = -length / 2.0f;
        vertices[3] =  width / 2.0f;

        vertices[4] =  length / 2.0f;
        vertices[5] =  width / 2.0f;

        vertices[6] =  length / 2.0f;
        vertices[7] = -width / 2.0f;
        return vertices;
    }

    public float getLength() {
        return length;
    }

    public float getWidth() {
        return width;
    }
}


