package com.accenture.gameoftanks.core;

import java.io.Serializable;

public class Building extends Entity implements Serializable {

    private final String textureName;

    Building(boolean isBreakable, int startingHp, String textureName) {
        super(true, isBreakable, 1.0e10f, 1.0e10f, startingHp);
        this.textureName = textureName;
    }

    public String getTextureName() {
        return textureName;
    }
}
