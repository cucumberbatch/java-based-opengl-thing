package ecs.systems.processes;

import ecs.systems.Collision;
import ecs.systems.MeshCollider;
import ecs.systems.SystemHandler;

public interface ISystem
        extends IInit, IInput, IUpdate, IRender {

    default void registerCollision(SystemHandler handler, MeshCollider collider) { }
    default void onCollisionStart(Collision collision) { }
    default void onCollision(Collision collision) { }
    default void onCollisionEnd(Collision collision) { }
}
