package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Shape;

import java.io.Serializable;

public class Building extends Entity implements Serializable {

    Building(boolean isBreakable, int startingHp, float [] vertices,  String textureName) {
        super(true, isBreakable, 1.0e10f, 1.0e10f, startingHp, textureName);
        Shape topology = new Shape(vertices);
        setTopology(topology);
    }
}
