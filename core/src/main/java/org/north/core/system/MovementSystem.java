package org.north.core.system;

import org.north.core.architecture.ApplicationContext;
import org.north.core.architecture.ComponentHandler;
import org.north.core.architecture.Inject;
import org.north.core.scene.component.MovementComponent;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.UpdateProcess;

@ComponentHandler(MovementComponent.class)
public class MovementSystem extends AbstractSystem<MovementComponent> implements InitProcess<MovementComponent>, UpdateProcess<MovementComponent> {

    @Inject
    public MovementSystem(ApplicationContext context) {
        super(MovementComponent.class, context);
    }

    @Override
    public void init(MovementComponent movementComponent) {
        movementComponent.initialZ = movementComponent.getTransform().getPosition().z;
    }

    @Override
    public void update(MovementComponent movementComponent, float dt) {
        float acc = movementComponent.acc;
        float range = movementComponent.range;
        float initialZ = movementComponent.initialZ;


        movementComponent.getTransform().moveTo(
                movementComponent.getTransform().getPosition().x,
                movementComponent.getTransform().getPosition().y,
                initialZ + (float) Math.sin(acc) * range
        );

        movementComponent.acc += dt * 2;

    }
}
