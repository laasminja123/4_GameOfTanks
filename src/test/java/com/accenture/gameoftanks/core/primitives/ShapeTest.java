package com.accenture.gameoftanks.core.primitives;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class ShapeTest {
    @Test
    public void ShapeTest() {
        //Created list of test shapes
        List<Shape> shapes = new LinkedList<>();
        shapes.add(new Shape(new float[]{10, 2, 14, 6, 10, 12, 6, 7}));
        shapes.add(new Shape(new float[]{10, 2, 14, 6, 10, 12, 6, 7, 21}));
        shapes.add(new Shape(new float[]{}));
        //Got shape object from list
        Shape TestShape = shapes.get(0);
        Shape TestShape1 = shapes.get(1);
        Shape TestShape2 = shapes.get(2);
        // 1 check x
        Vertex [] vertices = TestShape.getVertices();
        assertEquals("Expected 10, actual is: " + vertices[0].x, 10, vertices[0].x, 1.0e-7f);
        // 2 check y
        assertEquals("Expected 12, actual is: " + vertices[2].y, 12, vertices[2].y, 1.0e-7f);
        // 3 check value needs to be null
        assertArrayEquals(null, TestShape1.getVertices());
        assertArrayEquals(null, TestShape2.getVertices());
    }
}