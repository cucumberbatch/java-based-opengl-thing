package org.north.core.system;

import org.lwjgl.glfw.GLFW;
import org.north.core.component.PlayerControls;
import org.north.core.component.RigidBody;
import org.north.core.entity.Entity;
import org.north.core.component.Transform;
import org.north.core.context.ApplicationContext;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.system.process.InputHandleProcess;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.UpdateProcess;

@ComponentHandler(PlayerControls.class)
public class PlayerControlsSystem extends AbstractSystem<PlayerControls>
        implements InitProcess<PlayerControls>, InputHandleProcess<PlayerControls>, UpdateProcess<PlayerControls> {

    private Entity physicalBody;
    private RigidBody rigidBody;
    private boolean gravitationalState = false;
    private boolean keyIIsPressed;

    @Inject
    public PlayerControlsSystem(ApplicationContext context) {
        super(PlayerControls.class, context);
    }

    @Override
    public void init(PlayerControls playerControls) {
        rigidBody = cm.get(sceneRoot.getByName("referenceBox"), RigidBody.class);
    }

    @Override
    public void handleInput(PlayerControls component, Input input) {
        keyIIsPressed = input.isPressed(GLFW.GLFW_KEY_I);
    }

    @Override
    public void update(PlayerControls playerControls, float deltaTime) {
        if (keyIIsPressed) {
            gravitationalState = !gravitationalState;
            rigidBody.isGravitational = gravitationalState;
            rigidBody.velocity.set(0);
            rigidBody.acceleration.set(0);
            cm.get(physicalBody, Transform.class).moveTo(0f, 0f, 0f);
        }
    }
}
