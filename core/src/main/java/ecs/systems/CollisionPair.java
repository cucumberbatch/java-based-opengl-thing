package ecs.systems;

import vectors.Vector3f;

public class CollisionPair {
    public Vector3f A;
    public Vector3f B;
    public Vector3f normal;
    public float depth;
    public boolean hasCollision;

    public CollisionPair(Vector3f A, Vector3f B) {
        this.A = new Vector3f(A);
        this.B = new Vector3f(B);
        Vector3f direction = new Vector3f(A.x - B.x, A.y - B.y, A.z - B.z);
        this.normal = Vector3f.normalized(direction);
        this.depth  = direction.length();
    }
}
