package ecs.utils;

import ecs.shapes.ConvertibleShape;

public class ShapeUtils {
    public static float[] convertToVertices(ConvertibleShape shape) {
        return shape.convertToVertices();
    }
}
