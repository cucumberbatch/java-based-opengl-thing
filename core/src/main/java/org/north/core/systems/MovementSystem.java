package org.north.core.systems;

import org.north.core.components.MovementComponent;
import org.north.core.reflection.ComponentHandler;
import org.north.core.systems.processes.InitProcess;
import org.north.core.systems.processes.UpdateProcess;

@ComponentHandler(MovementComponent.class)
public class MovementSystem extends AbstractSystem<MovementComponent> implements InitProcess, UpdateProcess {

    @Override
    public void init() {
        component.initialZ = component.getTransform().position.z;
    }

    @Override
    public void update(float dt) {
        float acc = component.acc;
        float range = component.range;
        float initialZ = component.initialZ;


        component.getTransform().position.set(
                component.getTransform().position.x,
                component.getTransform().position.y,
                initialZ + (float) Math.sin(acc) * range
        );

        component.acc += dt * 2;

    }
}
