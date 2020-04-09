package ecs.util;

public class Physics {
    public static class Constants {
        public float gravityFactor = 9.81f;
        public Vector3 gravityVector = Vector3.mul(Vector3.down(), gravityFactor);
    }
}
