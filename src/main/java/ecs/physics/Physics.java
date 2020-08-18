package ecs.physics;

import ecs.math.Vector3f;

public class Physics {
    public static float gravityFactor = 9.81f;
    public static Vector3f gravityVector = Vector3f.down().mul(gravityFactor);
}
