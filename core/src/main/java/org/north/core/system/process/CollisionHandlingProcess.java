package org.north.core.system.process;

import org.north.core.component.Component;
import org.north.core.physics.collision.Collision;

public interface CollisionHandlingProcess<C extends Component> {

    void onCollisionStart(C component, Collision collision);
    void onCollision(C component, Collision collision);
    void onCollisionEnd(C component, Collision collision);

}
