package org.north.core.system;

import org.north.core.component.MovementComponent;
import org.north.core.context.ApplicationContext;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.UpdateProcess;

@ComponentHandler(MovementComponent.class)
public class MovementSystem extends AbstractSystem<MovementComponent> implements InitProcess<MovementComponent>, UpdateProcess<MovementComponent> {

    @Inject
    public MovementSystem(ApplicationContext context) {
        super(context);
    }

    @Override
    public void init(MovementComponent movementComponent) {
        movementComponent.initialZ = movementComponent.getTransform().position.z;
    }

    @Override
    public void update(MovementComponent movementComponent, float dt) {
        float acc = movementComponent.acc;
        float range = movementComponent.range;
        float initialZ = movementComponent.initialZ;


        movementComponent.getTransform().moveTo(
                movementComponent.getTransform().position.x,
                movementComponent.getTransform().position.y,
                initialZ + (float) Math.sin(acc) * range
        );

        movementComponent.acc += dt * 2;

    }
}
