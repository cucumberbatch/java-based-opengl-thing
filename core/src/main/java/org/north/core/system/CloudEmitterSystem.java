package org.north.core.system;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.north.core.component.*;
import org.north.core.architecture.entity.Entity;
import org.north.core.context.ApplicationContext;
import org.north.core.graphics.shader.AtlasTextureAnimationShader;
import org.north.core.graphics.Texture;
import org.north.core.reflection.ComponentHandler;
import org.north.core.reflection.di.Inject;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.UpdateProcess;

import static org.joml.Math.*;

@ComponentHandler(CloudEmitter.class)
public class CloudEmitterSystem extends AbstractSystem<CloudEmitter> implements InitProcess<CloudEmitter>, UpdateProcess<CloudEmitter> {

    private final Vector3f emittingPosition = new Vector3f(0f, -0.225f, 0.5f);
    private final Vector3f emittingScale = new Vector3f(0.2f, 0.2f, 0.2f);
    private final float twoPi = (float) (2 * PI);

    private float acc = 0;
    private long gasCloudEntityNumber = 0;

    private Entity world;
    private Transform worldTransform;
    private RigidBody movableWorldRigidBody;
    private Transform spaceshipTransform;
    private Transform spawnerTransform;

    @Inject
    public CloudEmitterSystem(ApplicationContext context) {
        super(context);
    }

    @Override
    public void init(CloudEmitter cloudEmitter) {
        world = et.getByName("movableWorld");
        worldTransform = world.transform;
        movableWorldRigidBody = world.get(RigidBody.class);
        spaceshipTransform = cloudEmitter.getTransform();
        spawnerTransform = et.getByName("gasCloudSpawner").transform;

        movableWorldRigidBody.isGravitational = false;
    }

    @Override
    public void update(CloudEmitter cloudEmitter, final float deltaTime) {
        boolean moving = false;

        float accelerationSpeed = 16;
        float rotationSpeed = 6;
        float rotationIncrement = deltaTime * rotationSpeed;
        float accelerationIncrement = deltaTime * accelerationSpeed;
        float angle = spaceshipTransform.rotation.z();

        if (Input.isHeldDown(GLFW.GLFW_KEY_A)) {
            angle = (spaceshipTransform.rotation.z - rotationIncrement) % twoPi;
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_D)) {
            angle = (spaceshipTransform.rotation.z + rotationIncrement) % twoPi;
        }

        spaceshipTransform.rotation.set(0, 0, angle);

        if (Input.isHeldDown(GLFW.GLFW_KEY_W)) {
            moving = true;
            movableWorldRigidBody.addImpulseToMassCenter(
                    accelerationIncrement * (float) sin(angle),
                    -accelerationIncrement * (float) cos(angle),
                    0f
            );
        }

        if (moving && acc > 1) {
            Entity gasCloudEntity = et.create("gas_cloud_" + gasCloudEntityNumber++);

            cm.take(gasCloudEntity)
                    .add(Transform.class, MeshRenderer.class, GasCloud.class);

            et.add(world, gasCloudEntity);

            Transform transform = gasCloudEntity.get(Transform.class);
            Vector3f worldPosition = worldTransform.getGlobalPosition(new Vector3f());
            worldPosition.set(-worldPosition.x / 5, -worldPosition.y / 5, worldPosition.z);//.add(emittingPosition);
            transform.position.set(worldPosition);
            transform.scale.set(emittingScale);

            MeshRenderer renderer = gasCloudEntity.get(MeshRenderer.class);
            renderer.shader = new AtlasTextureAnimationShader(6, 12, 12);
            renderer.texture = new Texture("core/src/main/resources/assets/textures/cloud-sprites-atlas.png");

            acc = 0;
        }

        acc += deltaTime * 6;
    }
}
