package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Shape;
import com.accenture.gameoftanks.core.primitives.Vertex;

import java.io.Serializable;

public class Entity implements Serializable {

    private int id;
    private final int startingHp;
    int currentHp;

    private final boolean isStatic;
    private final boolean isBreakable;
    private final float mass;
    private final float momentOfInertia;

    private Shape topology;
    Position position;
    Intent intent;

    Entity(boolean isStatic, boolean isBreakable, float mass, float momentOfInertia, int startingHp) {
        this.id = -1;
        this.isStatic = isStatic;
        this.isBreakable = isBreakable;
        this.mass = mass;
        this.momentOfInertia = momentOfInertia;
        this.startingHp = startingHp;
        this.currentHp = startingHp;

        this.position = new Position();
        this.intent = new Intent();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public boolean isAlive() {
        return currentHp > 0;
    }

    public int getStartingHp() {
        return startingHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void decreaseCurrentHp(int delta) {
        currentHp -= delta;

        if (currentHp < 0) {
            currentHp = 0;
        }
    }

    public boolean isStatic() {
        return isStatic;
    }

    public boolean isBreakable() {
        return isBreakable;
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
