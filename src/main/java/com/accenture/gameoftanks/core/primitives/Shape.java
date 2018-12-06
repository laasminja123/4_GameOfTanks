package com.accenture.gameoftanks.core.primitives;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static com.accenture.gameoftanks.core.MATH.*;

public class Shape implements Serializable {

    private Vertex [] vertices;
    private boolean isConvex;

    public Shape(float [] vertices) {
        if (vertices.length == 0 || vertices.length % 2 != 0) {
            return;
        }
        packVertices(vertices);
        computeConvexity();
    }

    private void packVertices(float [] vertices) {
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

    private void computeConvexity() {
        float [] mc = computeCenterOfMass();
        Edge edge = new Edge(null, null);
        float [] normal = new float[2];
        isConvex = true;

        for (int i = 0; i < vertices.length; i++) {
            if (i < vertices.length - 1) {
                edge.v1 = vertices[i];
                edge.v2 = vertices[i + 1];
            } else {
                edge.v1 = vertices[i];
                edge.v2 = vertices[0];
            }
            getNormal(normal, edge);
            float d0 = dot(normal, edge.v1);
            float d = dot(normal, mc);
            isConvex = isConvex & (d0 > d);
        }
    }

    private float [] computeCenterOfMass() {
        float xc = 0.0f;
        float yc = 0.0f;

        for (Vertex vertex: vertices) {
            xc += vertex.x;
            yc += vertex.y;
        }
        return new float[] {xc / vertices.length, yc / vertices.length};
    }

    public Vertex [] getVertices() {
        return vertices;
    }

    public boolean isConvex() {
        return isConvex;
    }
}
