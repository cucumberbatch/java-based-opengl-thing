package org.north.core.systems;

import org.lwjgl.glfw.GLFW;
import org.north.core.components.ComponentState;
import org.north.core.exception.ShaderUniformNotFoundException;
import org.north.core.scene.Scene;
import org.north.core.scene.SceneInitializer;
import org.north.core.architecture.ComponentManager;
import org.north.core.architecture.EntityManager;
import org.north.core.components.Camera;
import org.north.core.components.Component;
import org.north.core.entities.Entity;
import org.north.core.exception.ComponentNotFoundException;
import org.north.core.graphics.Graphics;
import org.north.core.graphics.Window;
import org.north.core.managment.FrameTiming;
import org.north.core.managment.SystemManager;
import org.north.core.physics.Collidable;
import org.north.core.systems.processes.*;
import org.north.core.utils.Logger;
import org.north.core.utils.Stopwatch;
import org.joml.Vector3f;

import java.util.*;
import java.util.stream.Collectors;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class GameLogicUpdater implements ISystem, Runnable {

    private Window window;
    private SceneInitializer sceneInitializer;
    private Scene scene;
    private Graphics graphics;

    private boolean isUpdatePaused = false;

    public EntityManager    entityManager;
    public ComponentManager componentManager;
    public SystemManager systemManager;


    public GameLogicUpdater(Window window) {
        this.window = window;
        this.graphics = Graphics.getInstance(window);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void run() {
        // Logger.info("Game loop started");

        final FrameTiming timingContext = new FrameTiming();

        // todo: load scene from file (game data deserialization)
        loadScene(scene);

        while (window.shouldNotClose()) {
            try {
                timingContext.updateTiming();
                final float elapsedTime = timingContext.getElapsedTime();

                applyDeferredCommands();
                updateInput();
                init();
                update(elapsedTime);
                registerCollisions();
                handleCollisions();
                render(window);

                timingContext.sync();
            } catch (RuntimeException e) {
                // Logger.error("Exiting game loop in case of thrown exception: " + e.getMessage(), e);
                break;
            }
        }

        // Logger.info("Game loop ended");
    }

    public void setDataManagers(EntityManager entityManager, ComponentManager componentManager, SystemManager systemManager) {
        this.entityManager = entityManager;
        this.componentManager = componentManager;
        this.systemManager = systemManager;
    }

    private void loadScene(Scene scene) {
        sceneInitializer = new SceneInitializer(scene);
        sceneInitializer.initSceneInUpdater(this);
//        sceneInitializer.readSceneFromFile("transform_temp");
    }

    @Override
    public void init() throws RuntimeException {
        // todo: needs to check for adding new components in runtime and checks for running out of components
        //  to initialize, so we can skip iteration moment in init() method call until the next component addition
        //
        //    if (systemManager.hasNoComponentsToInit()) return;
        //

        for (InitProcess process : systemManager.listOfSystemsForInit) {
            System<? extends Component> system = (System<? extends Component>) process;
            Iterator<? extends Component> iterator = system.getComponentIterator();
            while (iterator.hasNext()) {
                Component component = iterator.next();
                if (component.inState(ComponentState.READY_TO_INIT_STATE)) {
                    // Logger.trace(String.format(
//                            "Handling init component [%s: %s]",
//                            system.getClass().getName(),
//                            component.getEntity().getName())
//                    );
                    try {
                        system.setCurrentComponent(component);
                        process.init();
                        component.setState(ComponentState.READY_TO_OPERATE_STATE);
                    } catch (ComponentNotFoundException | NullPointerException e) {
                        // Logger.error(e);
                        component.setState(ComponentState.LATE_INIT_STATE);
                    }
                } else if (component.inState(ComponentState.LATE_INIT_STATE)) {
                    component.setState(ComponentState.READY_TO_INIT_STATE);
                }
            }
        }
    }

    public void updateInput() {
        Input.updateInput();

        if (Input.isHeldDown(GLFW.GLFW_KEY_P)) {
            isUpdatePaused = !isUpdatePaused;
//            Input.holdenKeys[GLFW.GLFW_KEY_P] = false;
            Input.holdenKeys.set(GLFW.GLFW_KEY_P, false);
        }
    }

    //todo: performance
    @SuppressWarnings("unchecked")
    public void registerCollisions() {
        if (systemManager.listOfSystemsForCollision.isEmpty()) return;

        Stopwatch.start();

        List<MeshCollider> componentList;
        CollisionPair pair;
        Collision collision;
        Collision previousFrameCollision;
        MeshCollider that;
        MeshCollider other;
        Entity A;
        Entity B;
        Vector3f positionA;
        Vector3f positionB;
        byte collisionState;
        int componentListSize;
        int collisionsListSize;
        boolean isCollisionFound;
        int lindex;
        int rindex;
        int cindex;

        collisionsListSize = systemManager.collisions.size();

        for (System<? extends Component> system: systemManager.listOfSystemsForCollision) {
            componentList = (List<MeshCollider>) system.getComponentList();
            componentListSize = componentList.size();
            for (lindex = 0; lindex < componentListSize - 1; lindex++) {
                that = componentList.get(lindex);
                for (rindex = lindex + 1; rindex < componentListSize; rindex++) {
                    other = componentList.get(rindex);
                    A = that.entity;
                    B = other.entity;
                    if (that.isStatic && other.isStatic) continue;
                    if (that.body == null || other.body == null) continue;
                    if (that.body.isIntersects(other.body)) {
                        positionA = A.transform.position;
                        positionB = B.transform.position;
                        isCollisionFound = false;
                        for (cindex = 0; cindex < collisionsListSize; cindex++) {
                            previousFrameCollision = systemManager.collisions.get(cindex);
                            if (isSameCollisionAsInPreviousFrame(previousFrameCollision, A, B)) {
                                isCollisionFound = true;
                                if (Collision.ENTERED == previousFrameCollision.state) {
                                    //todo: add collision pool
                                    previousFrameCollision.pair = new CollisionPair(positionA, positionB);
                                    previousFrameCollision.state = Collision.HOLD;
                                    previousFrameCollision.isModified = true;
                                    // Logger.info(String.format(
//                                            "Collision modified! a1: %s\tstate: %s",
//                                            previousFrameCollision,
//                                            previousFrameCollision.state
//                                    ));
                                }
                                break;
                            }
                        }
                        if (!isCollisionFound) {
                            collisionState = Collision.ENTERED;
                            //todo: add collision pool
                            pair      = new CollisionPair(positionA, positionB);
                            collision = new Collision(A, B, pair, collisionState);
                            collision.isModified = true;
                            systemManager.collisions.add(collision);
                            collisionsListSize++;
                            // Logger.info(String.format(
//                                    "Collision added! a1: %s\tstate: %s",
//                                    collision,
//                                    collision.state
//                            ));
                        }
                    } else {
                        for (cindex = 0; cindex < collisionsListSize; cindex++) {
                            previousFrameCollision = systemManager.collisions.get(cindex);
                            if (isSameCollisionAsInPreviousFrame(previousFrameCollision, A, B)) {
                                if (Collision.EXITED != previousFrameCollision.state) {
                                    previousFrameCollision.isModified = true;
                                    previousFrameCollision.state = Collision.EXITED;
                                    // Logger.info(String.format(
//                                            "Collision modified! a1: %s\tstate: %s",
//                                            previousFrameCollision,
//                                            previousFrameCollision.state
//                                    ));
                                } else {
                                    previousFrameCollision.isModified = false;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }

        for (cindex = collisionsListSize - 1; cindex >= 0; cindex--) {
            collision = systemManager.collisions.get(cindex);
            if (Collision.EXITED == collision.state && !collision.isModified) {
                systemManager.collisions.remove(cindex);
                collisionsListSize--;
                // Logger.info(String.format(
//                        "Collision removed! a1: %s\tstate: %s",
//                        collision,
//                        collision.state
//                ));
            }
        }

        Stopwatch.stop("Collision register systems handling ended!");
    }

    private boolean isSameCollisionAsInPreviousFrame(Collision previousFrameCollision, Collidable A, Collidable B) {
        return (previousFrameCollision.A == A && previousFrameCollision.B == B) ||
               (previousFrameCollision.A == B && previousFrameCollision.B == A);
    }

    //todo: performance
    @Override
    public void update(final float deltaTime) {
        Stopwatch.start();
        glfwPollEvents();

        for (UpdateProcess process : systemManager.listOfSystemsForUpdate) {
            if (isUpdatePaused && !CameraControlsSystem.class.isAssignableFrom(process.getClass())) continue;
            System<? extends Component> system = (System<? extends Component>) process;
            Iterator<? extends Component> iterator = system.getComponentIterator();
            while (iterator.hasNext()) {
                Component component = iterator.next();
                if (component.isActive() && component.inState(ComponentState.READY_TO_OPERATE_STATE)) {
                    // Logger.trace(String.format(
//                            "Handling update component [%s: %s]",
//                            system.getClass().getName(),
//                            component.getEntity().getName())
//                    );
                    try {
                        system.setCurrentComponent(component);
                        process.update(deltaTime);
                    } catch (ComponentNotFoundException | NullPointerException e) {
                        // Logger.error(e);
                        component.setState(ComponentState.READY_TO_INIT_STATE);
                    }
                }
            }
        }

        Stopwatch.stop("Update systems handling ended!");
    }

    //todo: performance
    private void handleCollisionEnter() {
        Stopwatch.start();

        for (CollisionHandlingProcess process : systemManager.listOfSystemsForCollisionHandling) {
            System<? extends Component> system = (System<? extends Component>) process;
            Iterator<? extends Component> iterator = system.getComponentIterator();
            while (iterator.hasNext()) {
                Component component = iterator.next();
                system.setCurrentComponent(component);
                for (Collision collision : systemManager.collisions) {
                    // Logger.trace(String.format("Visiting enter collision [%s] for component [%s]", collision, component));
                    if (collision.A != (component).getEntity() && collision.B != component.getEntity()) continue;
                    if (Collision.ENTERED == collision.state) {
                        if (component.getEntity() == collision.A) swapCollisionEntities(collision);
                        process.onCollisionStart(collision);
                    }
                }
            }
        }

        Stopwatch.stop("Collision enter handling ended!");
    }

    //todo: performance
    private void handleCollisionHold() {
        Stopwatch.start();

        for (CollisionHandlingProcess process : systemManager.listOfSystemsForCollisionHandling) {
            System<? extends Component> system = (System<? extends Component>) process;
            Iterator<? extends Component> iterator = system.getComponentIterator();
            while (iterator.hasNext()) {
                Component component = iterator.next();
                system.setCurrentComponent(component);
                for (Collision collision : systemManager.collisions) {
                    // Logger.trace(String.format("Visiting hold collision [%s] for component [%s]", collision, component));
                    if (collision.A != component.getEntity() && collision.B != component.getEntity()) continue;
                    if (Collision.HOLD == collision.state) {
                        if (component.getEntity() == collision.A) swapCollisionEntities(collision);
                        process.onCollision(collision);
                    }
                }
            }
        }

        Stopwatch.stop("Collision hold handling ended!");
    }

    //todo: performance
    private void handleCollisionExit() {
        Stopwatch.start();

        for (CollisionHandlingProcess process : systemManager.listOfSystemsForCollisionHandling) {
            System<? extends Component> system = (System<? extends Component>) process;
            Iterator<? extends Component> iterator = system.getComponentIterator();
            while (iterator.hasNext()) {
                Component component = iterator.next();
                system.setCurrentComponent(component);
                for (Collision collision : systemManager.collisions) {
                    // Logger.trace(String.format("Visiting exit collision [%s] for component [%s]", collision, component));
                    if (collision.A != component.getEntity() && collision.B != component.getEntity()) continue;
                    if (Collision.EXITED == collision.state) {
                        if (component.getEntity() == collision.A) swapCollisionEntities(collision);
                        process.onCollisionEnd(collision);
                    }
                }
            }
        }

        Stopwatch.stop("Collision exit handling ended!");
    }

    private void swapCollisionEntities(Collision collision) {
        Collidable temp = collision.B;
        collision.B = collision.A;
        collision.A = temp;
    }

    public void handleCollisions() {
        handleCollisionEnter();
        handleCollisionHold();
        handleCollisionExit();
    }

    public void render(Window window) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Stopwatch.start();

        for (RenderProcess process : systemManager.listOfSystemsForRender) {
            System<? extends Component> system = (System<? extends Component>) process;
            List<? extends Component> components = system.getComponentList();
            systemManager.sortComponentsByDistanceToCamera(components);
            if (MeshRendererSystem.class.isAssignableFrom(system.getClass())) {
                // Logger.debug("rendering order: " + components.stream().map(component -> component.getEntity().getName()).collect(Collectors.toList()));
            }
            for (Component component : components) {
                if (component.inState(ComponentState.READY_TO_OPERATE_STATE)) {
                    // Logger.trace(String.format(
//                            "Handling render component [%s: %s]",
//                            system.getClass().getName(),
//                            component.getEntity().getName())
//                    );
                    try {
                        system.setCurrentComponent(component);
                        process.render(graphics);
                    } catch (ComponentNotFoundException | NullPointerException e) {
                        // Logger.error(e);
                        component.setState(ComponentState.READY_TO_INIT_STATE);
                    } catch (ShaderUniformNotFoundException e) {
                        // Logger.error(String.format("Error while trying to find shader uniform with name '%s' in shader '%s'",
//                                e.getUniformName(), e.getShaderName()));
                        throw e;
                    }
                }
            }
        }

        Stopwatch.stop("Graphics systems handling ended!");

        Stopwatch.start();
        glfwSwapBuffers(window.getWindow());
        Stopwatch.stop("Graphics buffer swap ended!");
    }


    private void applyDeferredCommands() {
        systemManager.applyDeferredCommands();
    }

}
