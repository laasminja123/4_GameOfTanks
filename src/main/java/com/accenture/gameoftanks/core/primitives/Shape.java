package com.accenture.gameoftanks.core.primitives;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Shape implements Serializable {

    private Vertex [] vertices;

    public Shape(float [] vertices) {
        if (vertices.length == 0 || vertices.length % 2 != 0) {
            return;
        }
        this.vertices = new Vertex[vertices.length / 2];
        List<Float> coords = new LinkedList<>();
        int pos = 0;

        for (float value: vertices) {
            if (pos % 2 == 0) {
                coords.clear();
            }
            coords.add(value);
            pos++;

            if (pos % 2 == 0) {
                this.vertices[pos / 2 - 1] = new Vertex(coords.get(0), coords.get(1));
            }
        }
    }

    public Vertex [] getVertices() {
        return vertices;
    }
}
