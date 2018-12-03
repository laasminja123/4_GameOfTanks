package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Shape;
import com.accenture.gameoftanks.core.primitives.Vertex;

import java.io.Serializable;

public class Turret implements Serializable {

    private final float length;
    private final float width;
    private final float gunLength;
    private final float gunWidth;
    private float angle;
    private float offset;  // turret displacement from tank center position in longitudinal direction

    private Shape turretTopology;
    private Shape gunTopology;

    Turret(float length, float width, float offset, float gunLength, float gunWidth) {
        this.length = length;
        this.width = width;
        this.offset = offset;
        this.gunLength = gunLength;
        this.gunWidth = gunWidth;
        this.angle = 0.0f;

        turretTopology = new Shape(createTurretTopology(length, width));
        gunTopology = new Shape(createGunTopology(gunLength, gunWidth));
    }

    private float [] createTurretTopology(float length, float width) {
        float [] vertices = new float[8];

        vertices[0] = -length / 2.0f;
        vertices[1] = -width / 2.0f;

        vertices[2] =  length / 2.0f;
        vertices[3] = -width / 2.0f;

        vertices[4] =  length / 2.0f;
        vertices[5] =  width / 2.0f;

        vertices[6] = -length / 2.0f;
        vertices[7] =  width / 2.0f;
        return vertices;
    }

    private float [] createGunTopology(float length, float width) {
        float [] vertices = new float[8];

        vertices[0] =  0.0f;
        vertices[1] = -width / 2.0f;

        vertices[2] =  length / 2.0f;
        vertices[3] = -width / 2.0f;

        vertices[4] =  length / 2.0f;
        vertices[5] =  width / 2.0f;

        vertices[6] =  0.0f;
        vertices[7] =  width / 2.0f;
        return vertices;
    }

    public float getLength() {
        return length;
    }

    public float getWidth() {
        return width;
    }

    public float getOffset() {
        return offset;
    }

    public float getGunLength() {
        return gunLength;
    }

    public float getGunWidth() {
        return gunWidth;
    }

    void incrementTurretDirection(float delta) {
        angle += delta;

        if (angle > 2.0f * Math.PI) {
            angle -= 2.0f * Math.PI;
        } else if (angle < -2.0f * Math.PI) {
            angle += 2.0f * Math.PI;
        }
    }

    public float getAngle() {
        return angle;
    }

    public Vertex[] getTurretTopology() {
        return turretTopology.getVertices();
    }

    public Vertex[] getGunTopology() {
        return gunTopology.getVertices();
    }
}