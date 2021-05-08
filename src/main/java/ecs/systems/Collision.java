package ecs.systems;

import ecs.entities.Entity;

public class Collision {
    public Entity A;
    public Entity B;
    public CollisionPair pair;

    public Collision(Entity A, Entity B, CollisionPair pair) {
        register(A, B, pair);
    }

    public Collision register(Entity A, Entity B, CollisionPair pair) {
        this.A = A;
        this.B = B;
        this.pair = pair;
        return this;
    }
}
