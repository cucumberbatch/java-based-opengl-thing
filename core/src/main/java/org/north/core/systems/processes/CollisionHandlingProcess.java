package org.north.core.systems.processes;

import org.north.core.systems.Collision;

public interface CollisionHandlingProcess {

    void onCollisionStart(Collision collision);
    void onCollision(Collision collision);
    void onCollisionEnd(Collision collision);

}
