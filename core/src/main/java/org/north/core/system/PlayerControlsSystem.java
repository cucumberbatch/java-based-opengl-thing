package org.north.core.system;

import org.lwjgl.glfw.GLFW;
import org.north.core.component.PlayerControls;
import org.north.core.component.RigidBody;
import org.north.core.architecture.entity.Entity;
import org.north.core.context.ApplicationContext;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.UpdateProcess;

@ComponentHandler(PlayerControls.class)
public class PlayerControlsSystem extends AbstractSystem<PlayerControls>
        implements InitProcess<PlayerControls>, UpdateProcess<PlayerControls> {

    private Entity physicalBody;
    private RigidBody rigidBody;
    private boolean gravitationalState = false;

    @Inject
    public PlayerControlsSystem(ApplicationContext context) {
        super(context);
    }

    @Override
    public void init(PlayerControls playerControls) {
        physicalBody = em.getByName("referenceBox");
        rigidBody = physicalBody.get(RigidBody.class);
    }

    @Override
    public void update(PlayerControls playerControls, float deltaTime) {
        if (Input.isPressed(GLFW.GLFW_KEY_I)) {
            gravitationalState = !gravitationalState;
            rigidBody.isGravitational = gravitationalState;
            rigidBody.velocity.set(0);
            rigidBody.acceleration.set(0);
            physicalBody.transform.moveTo(0f, 0f, 0f);
        }
    }
}
