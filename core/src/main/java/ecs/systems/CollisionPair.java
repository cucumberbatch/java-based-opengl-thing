package ecs.systems;

import vectors.Vector2f;

public class CollisionPair {
    public Vector2f A;
    public Vector2f B;
    public Vector2f normal;
    public float depth;
    public boolean hasCollision;

    public CollisionPair(Vector2f A, Vector2f B) {
        this.A = new Vector2f(A);
        this.B = new Vector2f(B);
        Vector2f direction = new Vector2f(A.x - B.x, A.y - B.y);
        this.normal.set(Vector2f.normalized(direction));
        this.depth = direction.length();
    }
}
