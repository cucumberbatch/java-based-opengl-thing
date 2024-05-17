package org.north.core.system.process;

import org.north.core.component.Component;
import org.north.core.physics.collision.Collision;

public interface CollisionHandlingProcess<ComponentInstance extends Component> {

    void onCollisionStart(ComponentInstance component, Collision collision);
    void onCollision(ComponentInstance component, Collision collision);
    void onCollisionEnd(ComponentInstance component, Collision collision);

}
