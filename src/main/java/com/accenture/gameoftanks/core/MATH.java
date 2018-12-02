package com.accenture.gameoftanks.core;

import com.accenture.gameoftanks.core.primitives.Edge;
import com.accenture.gameoftanks.core.primitives.Vertex;

import static java.lang.Math.*;

public class MATH {

    private MATH(){}

    /*
    public static void rotate2d(float angle, Vertex... vertices) {
        for (Vertex vertex: vertices) {
            float x2 = (float) (vertex.x * cos(angle) + vertex.y * -sin(angle));
            float y2 = (float) (vertex.x * sin(angle) + vertex.y *  cos(angle));
            vertex.xt = x2;
            vertex.yt = y2;
        }
    }
    */

    public static void transform2d(Position position, Vertex... vertices) {
        for (Vertex vertex: vertices) {
            float x2 = (float) (vertex.x * cos(position.alpha) + vertex.y * -sin(position.alpha));
            float y2 = (float) (vertex.x * sin(position.alpha) + vertex.y *  cos(position.alpha));
            vertex.xt = position.posX + x2;
            vertex.yt = position.posY + y2;
        }
    }

    /*
    public static void getNormal(float [] normal, Vertex v1, Vertex v2) {
        normal[0] = -(v2.yt - v1.yt);
        normal[1] = v2.xt - v1.xt;

        // normalize
        float mag = (float) sqrt(normal[0] * normal[0] + normal[1] * normal[1]);
        normal[0] /= mag;
        normal[1] /= mag;
    }
    */

    public static void getNormal(float [] normal, Edge edge) {
        normal[0] = -(edge.v2.yt - edge.v1.yt);
        normal[1] = edge.v2.xt - edge.v1.xt;

        // normalize
        float mag = (float) sqrt(normal[0] * normal[0] + normal[1] * normal[1]);
        normal[0] /= mag;
        normal[1] /= mag;
    }

    /*
    public static void normalize(float [] direction, Edge edge) {
        float dx = edge.v2.xt - edge.v1.xt;
        float dy = edge.v2.yt - edge.v1.yt;
        float mag = (float) sqrt(dx * dx + dy * dy);

        direction[0] = dx / mag;
        direction[1] = dy / mag;
    }
    */

    public static void normalize(float [] direction, float vx, float vy) {
        float mag = (float) sqrt(vx * vx + vy * vy);

        direction[0] = vx / mag;
        direction[1] = vy / mag;
    }

    /*
    public static void reverse(float [] vector) {
        vector[0] *= -1.0f;
        vector[1] *= -1.0f;
    }
    */

    public static void scale(float factor, float [] vector) {
        vector[0] *= factor;
        vector[1] *= factor;
    }

    public static void rotate(float angle, float [] vector) {
        float x2 = (float) (vector[0] * cos(angle) + vector[1] * -sin(angle));
        float y2 = (float) (vector[0] * sin(angle) + vector[1] *  cos(angle));
        vector[0] = x2;
        vector[1] = y2;
    }

    public static void add(float [] v1, float [] v2) {
        v1[0] += v2[0];
        v1[1] += v2[1];
    }

    /**
     *
     * @param edge1 first edge
     * @param edge2 second edge
     * @return -1.0f if NOT intersects, and positive value which is penetration depth if intersects
     */
    public static float intersects(Edge edge1, Edge edge2) {
        float d0, d1, d2;
        float d = 0.0f;
        float [] normal = new float[2];

        // compute first edge positions relative to second edge -----------------------------------
        getNormal(normal, edge2);

        // distance to second edge
        d0 = dot(normal, edge2.v1);

        // distance to first edge start vertex
        d1 = dot(normal, edge1.v1);

        // distance to first edge end vertex
        d2 = dot(normal, edge1.v2);

        boolean intersects1 = ((d2 - d0) * (d1 - d0) < 0.0f) || (d1 == d0) || (d2 == d0);

        if (intersects1) {
            d = abs(d2 - d0);
        }

        // compute second edge positions relative to first edge -----------------------------------
        getNormal(normal, edge1);

        // distance to second edge
        d0 = dot(normal, edge1.v1);

        // distance to first edge start vertex
        d1 = dot(normal, edge2.v1);

        // distance to first edge end vertex
        d2 = dot(normal, edge2.v2);

        boolean intersects2 = (d2 - d0) * (d1 - d0) < 0.0f;

        if (intersects1 && intersects2) {
            return d;
        } else {
            return -1.0f;
        }
    }

    public static float dot(float [] v1, float [] v2) {
        return v1[0] * v2[0] + v1[1] * v2[1];
    }

    public static float dot(float [] v1, Vertex v2) {
        return v1[0] * v2.xt + v1[1] * v2.yt;
    }

    public static float dot(float [] v1, float v2x, float v2y) {
        return v1[0] * v2x + v1[1] * v2y;
    }

    /**
     * method adopted for 2D vectors
     * @param v1 float x2
     * @param v2 float x2
     * @return value, which is orthogonal to XY plane
     */
    public static float cross2D(float [] v1, float [] v2) {
        return (v1[0] * v2[1] - v2[0] * v1[1]);
    }
}
