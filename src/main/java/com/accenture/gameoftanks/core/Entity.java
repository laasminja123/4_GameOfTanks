package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Shape;
import com.accenture.gameoftanks.core.primitives.Vertex;

import java.io.Serializable;

public class Entity implements Serializable {

    private final boolean isStatic;
    private final float mass;
    private final float momentOfInertia;

    private Shape topology;
    Position position;
    Intent intent;

    Entity(boolean isStatic, float mass, float momentOfInertia) {
        this.isStatic = isStatic;
        this.mass = mass;
        this.momentOfInertia = momentOfInertia;

        this.position = new Position();
        this.intent = new Intent();
    }

    public boolean isStatic() {
        return isStatic;
    }

    public float getMass() {
        return mass;
    }

    public float getMomentOfInertia() {
        return momentOfInertia;
    }

    public Vertex [] getTopology() {
        return topology.getVertices();
    }

    void setTopology(Shape topology) {
        this.topology = topology;
    }

    public Position getPosition() {
        return position;
    }

    public Intent getIntent() {
        return intent;
    }
}
