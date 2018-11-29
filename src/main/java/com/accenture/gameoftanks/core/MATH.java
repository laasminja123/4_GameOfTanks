package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Edge;
import com.accenture.gameoftanks.core.primitives.Vertex;

import static java.lang.Math.*;

public class MATH {

    private MATH(){}

    public static void rotate2d(float angle, Vertex... vertices) {
        for (Vertex vertex: vertices) {
            float x2 = (float) (vertex.x * cos(angle) + vertex.y * -sin(angle));
            float y2 = (float) (vertex.x * sin(angle) + vertex.y *  cos(angle));
            vertex.xt = x2;
            vertex.yt = y2;
        }
    }

    public static void transform2d(Position position, Vertex... vertices) {
        for (Vertex vertex: vertices) {
            float x2 = (float) (vertex.x * cos(position.alpha) + vertex.y * -sin(position.alpha));
            float y2 = (float) (vertex.x * sin(position.alpha) + vertex.y *  cos(position.alpha));
            vertex.xt = position.posX + x2;
            vertex.yt = position.posY + y2;
        }
    }

    public static void getNormal(float [] normal, Vertex v1, Vertex v2) {
        normal[0] = -(v2.yt - v1.yt);
        normal[1] = v2.xt - v1.xt;

        // normalize
        float mag = (float) sqrt(normal[0] * normal[0] + normal[1] * normal[1]);
        normal[0] /= mag;
        normal[1] /= mag;
    }

    public static void getNormal(float [] normal, Edge edge) {
        normal[0] = -(edge.v2.yt - edge.v1.yt);
        normal[1] = edge.v2.xt - edge.v1.xt;

        // normalize
        float mag = (float) sqrt(normal[0] * normal[0] + normal[1] * normal[1]);
        normal[0] /= mag;
        normal[1] /= mag;
    }

    /**
     *
     * @param edge1 first edge
     * @param edge2 second edge
     * @return 0.0f if there is no intersection, positive value which is depth of penetration of first edge
     * inside second edge
     */
    public static float intersects(Edge edge1, Edge edge2) {
        float [] normal = new float[2];
        getNormal(normal, edge2);
        float [] direction = new float[2];

        // compute first edge positions relative to second edge
        //float d1 =
        return 0.0f;
    }

    public static float dotProduct(float [] v1, float [] v2) {
        return v1[0] * v2[0] + v1[1] * v2[1];
    }
}
