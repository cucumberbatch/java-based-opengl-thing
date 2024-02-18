package org.north.core.systems;

import org.lwjgl.glfw.GLFW;
import org.north.core.components.PlayerControls;
import org.north.core.components.RigidBody;
import org.north.core.architecture.entity.Entity;
import org.north.core.context.ApplicationContext;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.systems.processes.InitProcess;
import org.north.core.systems.processes.UpdateProcess;

import java.io.*;

@ComponentHandler(PlayerControls.class)
public class PlayerControlsSystem extends AbstractSystem<PlayerControls>
        implements InitProcess, UpdateProcess {

    private Entity physicalBody;
    private boolean gravitationalState = false;

    @Inject
    public PlayerControlsSystem(ApplicationContext context) {
        super(context);
    }


    @Override
    public void init() {
        physicalBody = em.getByName("referenceBox");
    }

    @Override
    public void update(float deltaTime) {
        RigidBody rigidBody = physicalBody.getComponent(RigidBody.class);

        if (Input.isPressed(GLFW.GLFW_KEY_I)) {
            gravitationalState = !gravitationalState;
            rigidBody.isGravitational = gravitationalState;
            rigidBody.velocity.set(0);
            rigidBody.acceleration.set(0);
            physicalBody.transform.moveTo(0f, 0f, 0f);
        }

        if (Input.isPressed(GLFW.GLFW_KEY_K)) {
            File file = new File("transform_temp");

            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
                outputStream.writeObject(component.getTransform());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
