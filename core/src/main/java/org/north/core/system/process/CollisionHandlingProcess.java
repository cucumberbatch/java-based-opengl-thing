package org.north.core.system.process;

import org.north.core.component.Component;
import org.north.core.physics.collision.Collision;

public interface CollisionHandlingProcess<C extends Component> extends Process {

    void onCollisionStarted(C component, Collision collision);
    void onCollisionContinued(C component, Collision collision);
    void onCollisionEnded(C component, Collision collision);

}
