package org.north.core.physics.collision;

import org.north.core.managment.data.Stateful;

public class Collision implements Stateful<CollisionState> {
    public Collidable A;
    public Collidable B;
    public CollisionPair pair;
    public CollisionState state;
    public boolean isModified;

    public Collision(CollisionState state, Collidable A, Collidable B, CollisionPair pair) {
        register(state, A, B, pair);
    }

    public Collision register(CollisionState state, Collidable A, Collidable B, CollisionPair pair) {
        this.state = state;
        this.A = A;
        this.B = B;
        this.pair = pair;
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

    @Override
    public CollisionState getState() {
        return state;
    }

    @Override
    public void setState(CollisionState collisionState) {
        this.state = collisionState;
    }
}
