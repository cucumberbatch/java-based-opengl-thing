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
import org.north.core.system.process.InputHandleProcess;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.UpdateProcess;

import static org.joml.Math.*;

@ComponentHandler(CloudEmitter.class)
public class CloudEmitterSystem extends AbstractSystem<CloudEmitter>
        implements InitProcess<CloudEmitter>, InputHandleProcess<CloudEmitter>, UpdateProcess<CloudEmitter> {
    private float acc = 0;
    private long  gasCloudEntityNumber = 0;

    private Entity    movableWorld;
    private Transform movableWorldTransform;
    private RigidBody movableWorldRigidBody;
    private Transform spaceshipTransform;
    private Transform spawnerTransform;

    private static final Vector3f emittingPosition = new Vector3f(0f, -0.225f, 0.5f);
    private static final Vector3f emittingScale    = new Vector3f(0.2f, 0.2f, 0.2f);
    private static final float    twoPi = (float) (2 * PI);

    private boolean keyAIsHolded;
    private boolean keyDIsHolded;
    private boolean keyWIsHolded;

    @Inject
    public CloudEmitterSystem(ApplicationContext context) {
        super(CloudEmitter.class, context);
    }

    @Override
    public void init(CloudEmitter cloudEmitter) {
        Entity emitterEntity = cloudEmitter.getEntity();
        Entity spawnerEntity = sceneRoot.getByName("gasCloudSpawner");
        movableWorld         = sceneRoot.getByName("movableWorld");
        
        spaceshipTransform    = cm.get(emitterEntity, Transform.class);
        spawnerTransform      = cm.get(spawnerEntity, Transform.class);
        movableWorldTransform = cm.get(movableWorld,  Transform.class);
        movableWorldRigidBody = cm.get(movableWorld,  RigidBody.class);

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
        float rotationSpeed     = 6;
        float rotationIncrement     = deltaTime * rotationSpeed;
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
                    +accelerationIncrement * (float) sin(angle),
                    -accelerationIncrement * (float) cos(angle),
                    0f
            );
        }

        if (moving && acc > 1) {
            Entity gasCloudEntity = new Entity("gas_cloud_" + gasCloudEntityNumber++);

            movableWorld.add(gasCloudEntity);

            //cm.add(gasCloudEntity, Transform.class, MeshRenderer.class, GasCloud.class);

            Vector3f globalPos = movableWorldTransform.getGlobalPosition(new Vector3f());
            Transform transform = new Transform();
            transform.moveTo(
                -globalPos.x / 5,
                -globalPos.y / 5,
                +globalPos.z
            );
            transform.rescaleTo(emittingScale);

            MeshRenderer renderer = new MeshRenderer();
            renderer.shader  = new AtlasTextureAnimationShader(6, 12, 12);
            renderer.texture = new Texture("core/src/main/resources/assets/textures/cloud-sprites-atlas.png");
            
            cm.add(gasCloudEntity, transform);
            cm.add(gasCloudEntity, renderer);

            cm.add(gasCloudEntity, new GasCloud());

            //Transform transform = cm.get(gasCloudEntity, Transform.class);
            //Vector3f  globalPos = worldTransform.getGlobalPosition(new Vector3f());

            //transform.moveTo(
            //    -globalPos.x / 5,
            //    -globalPos.y / 5,
            //    +globalPos.z
            //);
            //transform.rescaleTo(emittingScale);

            //MeshRenderer renderer = cm.get(gasCloudEntity, MeshRenderer.class);
            //renderer.shader  = new AtlasTextureAnimationShader(6, 12, 12);
            //renderer.texture = new Texture("core/src/main/resources/assets/textures/cloud-sprites-atlas.png");

            acc = 0;
        }

        acc += deltaTime * 6;
    }
}
