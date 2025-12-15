package org.north.core.system;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.north.core.architecture.entity.ComponentContainer;
import org.north.core.component.*;
import org.north.core.architecture.entity.Entity;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.shader.AtlasTextureAnimationShader;
import org.north.core.graphics.Texture;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.system.process.InputHandleProcess;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.UpdateProcess;

import static org.joml.Math.*;

@ComponentHandler(CloudEmitter.class)
public class CloudEmitterSystem extends AbstractSystem<CloudEmitter>
        implements InitProcess<CloudEmitter>, InputHandleProcess<CloudEmitter>, UpdateProcess<CloudEmitter> {
    private float acc = 0;
    private long gasCloudEntityNumber = 0;

    private Entity world;
    private Transform worldTransform;
    private RigidBody movableWorldRigidBody;
    private Transform spaceshipTransform;
    private Transform spawnerTransform;

    private static final Vector3f emittingPosition = new Vector3f(0f, -0.225f, 0.5f);
    private static final Vector3f emittingScale = new Vector3f(0.2f, 0.2f, 0.2f);
    private static final float twoPi = (float) (2 * PI);

    private final ComponentContainer componentContainer;

    private boolean keyAIsHolded;
    private boolean keyDIsHolded;
    private boolean keyWIsHolded;

    @Inject
    public CloudEmitterSystem(ApplicationContext context) {
        super(context);
        this.componentContainer = context.getDependency(ComponentContainer.class);
    }

    @Override
    public void init(CloudEmitter cloudEmitter) {
        spaceshipTransform = cloudEmitter.getTransform();
        spawnerTransform = sceneRoot.getByName("gasCloudSpawner").get(Transform.class);
        world = sceneRoot.getByName("movableWorld");
        worldTransform = world.get(Transform.class);
        movableWorldRigidBody = world.get(RigidBody.class);

        movableWorldRigidBody.isGravitational = false;
    }

    @Override
    public void handleInput(CloudEmitter component, Input input) {
        keyAIsHolded = input.isHeld(GLFW.GLFW_KEY_A);
        keyDIsHolded = input.isHeld(GLFW.GLFW_KEY_D);
        keyWIsHolded = input.isHeld(GLFW.GLFW_KEY_W);
    }

    @Override
    public void update(CloudEmitter cloudEmitter, final float deltaTime) {
        boolean moving = false;

        float accelerationSpeed = 15;
        float rotationSpeed = 6;
        float rotationIncrement = deltaTime * rotationSpeed;
        float accelerationIncrement = deltaTime * accelerationSpeed;
        float angle = spaceshipTransform.getRotation().z;

        if (keyAIsHolded) {
            angle = (spaceshipTransform.getRotation().z - rotationIncrement) % twoPi;
        }
        if (keyDIsHolded) {
            angle = (spaceshipTransform.getRotation().z + rotationIncrement) % twoPi;
        }

        spaceshipTransform.getRotation().set(0, 0, angle);

        if (keyWIsHolded) {
            moving = true;
            movableWorldRigidBody.addImpulseToMassCenter(
                    accelerationIncrement * (float) sin(angle),
                    -accelerationIncrement * (float) cos(angle),
                    0f
            );
        }

        if (moving && acc > 1) {
            Entity gasCloudEntity = new Entity("gas_cloud_" + gasCloudEntityNumber++, componentContainer);

            world.add(gasCloudEntity);

            cm.take(gasCloudEntity)
                    .add(Transform.class, MeshRenderer.class, GasCloud.class);

            Transform transform = gasCloudEntity.get(Transform.class);
            Vector3f globalPosition = worldTransform.getGlobalPosition(new Vector3f());
            transform.moveTo(-globalPosition.x / 5, -globalPosition.y / 5, globalPosition.z);
            transform.rescaleTo(emittingScale);

            MeshRenderer renderer = gasCloudEntity.get(MeshRenderer.class);
            renderer.shader = new AtlasTextureAnimationShader(6, 12, 12);
            renderer.texture = new Texture("core/src/main/resources/assets/textures/cloud-sprites-atlas.png");

            acc = 0;
        }

        acc += deltaTime * 6;
    }
}
