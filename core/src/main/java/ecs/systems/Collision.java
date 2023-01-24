package ecs.systems;

import ecs.entities.Entity;
import ecs.physics.Collidable;

public class Collision {
    public Collidable A;
    public Collidable B;
    public CollisionPair pair;
    public byte state;
    public boolean isModified;

    public static final byte ENTERED = 0;
    public static final byte HOLD    = 1;
    public static final byte EXITED  = 2;

    public Collision(Collidable A, Collidable B, CollisionPair pair, byte state) {
        register(A, B, pair, state);
    }

    public Collision register(Collidable A, Collidable B, CollisionPair pair, byte state) {
        this.A = A;
        this.B = B;
        this.pair = pair;
        this.state = state;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Collision collision = (Collision) o;

        return (this.A == collision.A && this.B == collision.B ||
                this.A == collision.B && this.B == collision.A);
    }

    @Override
    public int hashCode() {
        int result = A != null ? A.hashCode() : 0;
        result = 31 * result + (B != null ? B.hashCode() : 0);
        result = 31 * result + (pair != null ? pair.hashCode() : 0);
        return result;
    }
}
