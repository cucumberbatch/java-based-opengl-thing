package org.north.core.system.process;

import org.north.core.component.Component;
import org.north.core.physics.collision.Collision;

public interface CollisionHandlingProcess<ComponentInstance extends Component> extends Process {

    void onCollisionStarted(ComponentInstance component, Collision collision);
    void onCollisionContinued(ComponentInstance component, Collision collision);
    void onCollisionEnded(ComponentInstance component, Collision collision);

}
