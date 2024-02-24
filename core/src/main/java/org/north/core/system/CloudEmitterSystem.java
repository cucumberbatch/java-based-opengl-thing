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

import java.io.*;
import java.lang.reflect.InvocationTargetException;

@ComponentHandler(CloudEmitter.class)
public class CloudEmitterSystem extends AbstractSystem<CloudEmitter> implements InitProcess<CloudEmitter>, UpdateProcess<CloudEmitter> {

    private final Vector3f emittingPosition = new Vector3f(0f, -0.225f, 0.5f);
    private final Vector3f emittingScale = new Vector3f(0.2f, 0.2f, 0.2f);

    private float acc = 0;
    private long gasCloudEntityNumber = 0;
    private int gasCloudCount = 0;

    private Entity world;
    private Transform worldTransform;
    private RigidBody rigidBody;

    @Inject
    public CloudEmitterSystem(ApplicationContext context) {
        super(context);
    }

    @Override
    public void init(CloudEmitter cloudEmitter) {
        world = et.getByName("movableWorld");
        worldTransform = world.transform;
        rigidBody = world.get(RigidBody.class);

        rigidBody.isGravitational = false;
    }

    @Override
    public void update(CloudEmitter cloudEmitter, final float deltaTime) {
        boolean moving = false;
        float speed = 4;

        if (Input.isHeldDown(GLFW.GLFW_KEY_W)) {
            rigidBody.addImpulseToMassCenter(0f, -deltaTime * speed, 0f);
            moving = true;
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_S)) {
            rigidBody.addImpulseToMassCenter(0f, deltaTime * speed, 0f);
            moving = true;
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_A)) {
            rigidBody.addImpulseToMassCenter(-deltaTime * speed, 0f, 0f);
            moving = true;
        }
        if (Input.isHeldDown(GLFW.GLFW_KEY_D)) {
            rigidBody.addImpulseToMassCenter(deltaTime * speed, 0f, 0f);
            moving = true;
        }


        if (moving) {
            if (acc > 1) {
                Entity gasCloudEntity = et.create("gas_cloud_" + gasCloudEntityNumber++);

                cm.take(gasCloudEntity)
                        .add(Transform.class, MeshRenderer.class, GasCloud.class);

                et.add(world, gasCloudEntity);
//                gasCloudEntity.setParent(world);

                Transform transform = gasCloudEntity.get(Transform.class);
                MeshRenderer renderer = gasCloudEntity.get(MeshRenderer.class);

                transform.position.set(emittingPosition.sub(worldTransform.position, new Vector3f()));
                transform.scale.set(emittingScale);

                renderer.shader = new AtlasTextureAnimationShader(6, 12, 12);
                renderer.texture = new Texture("core/src/main/resources/assets/textures/cloud-sprites-atlas.png");

                gasCloudCount++;
                acc = 0;
            }
        }

        acc += deltaTime * 6;
    }
}