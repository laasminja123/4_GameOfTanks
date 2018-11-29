package com.accenture.gameoftanks.core.primitives;

import java.io.Serializable;

public class Vertex implements Serializable {
    // initial positions in model CS
    public float x;
    public float y;

    // transformed position in local CS
    public float xt;
    public float yt;

    public Vertex(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
