package ecs.systems.processes;

import ecs.systems.Collision;

public interface CollisionHandlingProcess {

    void onCollisionStart(Collision collision);
    void onCollision(Collision collision);
    void onCollisionEnd(Collision collision);

}
