package ecs.systems;

import ecs.GameLogic;
import ecs.Scene;
import ecs.SceneInitializer;
import ecs.architecture.ComponentManager;
import ecs.architecture.EntityManager;
import ecs.architecture.TreeEntityManager;
import ecs.components.AbstractComponent;
import ecs.components.Component;
import ecs.components.Transform;
import ecs.entities.Entity;
import ecs.exception.ComponentNotFoundException;
import ecs.gl.Window;
import ecs.managment.FrameTiming;
import ecs.managment.SystemManager;
import ecs.physics.Collidable;
import ecs.utils.Logger;
import vectors.Vector3f;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

@SuppressWarnings("rawtypes")
public class GameLogicUpdater implements GameLogic {

    private Window window;
    private SceneInitializer sceneInitializer;
    private Scene scene;

    public GameLogicUpdater(Window window) {
        this.window = window;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public EntityManager    entityManager;
    public ComponentManager componentManager;
    public SystemManager    systemManager;

    @Override
    public void run() {
        FrameTiming timingContext = new FrameTiming();

        loadScene(scene);

        while (window.shouldNotClose()) {
            try {
                timingContext.updateTiming();

                updateInput();
                init();
                update(timingContext.getElapsedTime());
                registerCollisions();
                handleCollisions();
                render(window);

                timingContext.sync();
            } catch (RuntimeException e) {
                Logger.error(e);
            }
        }
    }

    public void setDataManagers(EntityManager entityManager, ComponentManager componentManager, SystemManager systemManager) {
        this.entityManager = entityManager;
        this.componentManager = componentManager;
        this.systemManager = systemManager;
    }

    private void loadScene(Scene scene) {
        sceneInitializer = new SceneInitializer(scene);
        sceneInitializer.initSceneInUpdater(this);
    }

    @Override
    /* We are sure that each component object from system.getComponentList()
    is a Component type, because the only way of adding a component into
    component list is through component factory method call, that by definition
    generates objects of Component type */
    @SuppressWarnings("unchecked")
    public void init() throws RuntimeException {
        for (System system : systemManager.listOfSystemsForInit) {
            for (Component component : (List<Component>) system.getComponentList()) {
                if (component.getState() == AbstractComponent.READY_TO_INIT_STATE) {
                    Logger.trace(String.format(
                            "Handling init component [%s: %s]",
                            system.getClass().getName(),
                            component.getEntity().getName())
                    );
                    try {
                        system.setCurrentComponent(component);
                        system.init();
                        component.setState(AbstractComponent.READY_TO_OPERATE_STATE);
                    } catch (ComponentNotFoundException | NullPointerException e) {
                        Logger.error(e);
                        component.setState(AbstractComponent.LATE_INIT_STATE);
                    }
                } else if (component.getState() == AbstractComponent.LATE_INIT_STATE) {
                    component.setState(AbstractComponent.READY_TO_INIT_STATE);
                }
            }
        }
    }

    @Override
    public void updateInput() {
        Input.updateInput();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void registerCollisions() {
        if (systemManager.listOfSystemsForCollision.isEmpty()) return;

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

        ComponentManager componentManager = ComponentManager.getInstance();

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
                    if (that.mesh == null || other.mesh == null) return;
                    if (that.mesh.isIntersects(other.mesh)) {
                        positionA = componentManager.getComponent(A, Transform.class).position;
                        positionB = componentManager.getComponent(B, Transform.class).position;
                        isCollisionFound = false;
                        for (cindex = 0; cindex < collisionsListSize; cindex++) {
                            previousFrameCollision = systemManager.collisions.get(cindex);
                            if (isSameCollisionAsInPreviousFrame(previousFrameCollision, A, B)) {
                                isCollisionFound = true;
                                if (Collision.ENTERED == previousFrameCollision.state) {
                                    previousFrameCollision.pair = new CollisionPair(positionA, positionB);
                                    previousFrameCollision.state = Collision.HOLD;
                                    previousFrameCollision.isModified = true;
                                    Logger.info(String.format(
                                            "Collision modified! a1: %s\tstate: %s",
                                            previousFrameCollision,
                                            previousFrameCollision.state
                                    ));
                                }
                                break;
                            }
                        }
                        if (!isCollisionFound) {
                            collisionState = Collision.ENTERED;
                            pair      = new CollisionPair(positionA, positionB);
                            collision = new Collision(A, B, pair, collisionState);
                            collision.isModified = true;
                            systemManager.collisions.add(collision);
                            collisionsListSize++;
                            Logger.info(String.format(
                                    "Collision added! a1: %s\tstate: %s",
                                    collision,
                                    collision.state
                            ));
                        }
                    } else {
                        for (cindex = 0; cindex < collisionsListSize; cindex++) {
                            previousFrameCollision = systemManager.collisions.get(cindex);
                            if (isSameCollisionAsInPreviousFrame(previousFrameCollision, A, B)) {
                                if (Collision.EXITED != previousFrameCollision.state) {
                                    previousFrameCollision.isModified = true;
                                    previousFrameCollision.state = Collision.EXITED;
                                    Logger.info(String.format(
                                            "Collision modified! a1: %s\tstate: %s",
                                            previousFrameCollision,
                                            previousFrameCollision.state
                                    ));
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
                Logger.info(String.format(
                        "Collision removed! a1: %s\tstate: %s",
                        collision,
                        collision.state
                ));
            }
        }
    }

    private boolean isSameCollisionAsInPreviousFrame(Collision previousFrameCollision, Collidable A, Collidable B) {
        return (previousFrameCollision.A == A && previousFrameCollision.B == B) ||
               (previousFrameCollision.A == B && previousFrameCollision.B == A);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(float deltaTime) {
        long nanos = java.lang.System.nanoTime();
        glfwPollEvents();

        for (System system : systemManager.listOfSystemsForUpdate) {
            for (Component component : (List<Component>) system.getComponentList()) {
                if (component.getState() == AbstractComponent.READY_TO_OPERATE_STATE) {
                    Logger.trace(String.format(
                            "Handling update component [%s: %s]",
                            system.getClass().getName(),
                            component.getEntity().getName())
                    );
                    try {
                        system.setCurrentComponent(component);
                        system.update(deltaTime);
                    } catch (ComponentNotFoundException | NullPointerException e) {
                        Logger.error(e);
                        component.setState(AbstractComponent.READY_TO_INIT_STATE);
                    }
                }
            }
        }

        float diffMillis = (float) (java.lang.System.nanoTime() - nanos) / 1_000_000L;
        Logger.trace(String.format("Update systems handling ended! Spent time: <bold>%.3f[ms]</>", diffMillis));
    }

    private void handleCollisionEnter() {
        for (System system : systemManager.listOfSystemsForCollisionHandling) {
            for (Object component : system.getComponentList()) {
                system.setCurrentComponent((Component) component);
                for (Collision collision : systemManager.collisions) {
                    Logger.trace(String.format("Visiting enter collision [%s] for component [%s]", collision, component));
                    if (collision.A != ((Component) component).getEntity() && collision.B != ((Component) component).getEntity()) continue;
                    if (Collision.ENTERED == collision.state) {
                        if (((Component) component).getEntity() == collision.A) swapCollisionEntities(collision);
                        system.onCollisionStart(collision);
                    }
                }
            }
        }
    }

    private void handleCollisionHold() {
        for (System system : systemManager.listOfSystemsForCollisionHandling) {
            for (Object component : system.getComponentList()) {
                system.setCurrentComponent((Component) component);
                for (Collision collision : systemManager.collisions) {
                    Logger.trace(String.format("Visiting hold collision [%s] for component [%s]", collision, component));
                    if (collision.A != ((Component) component).getEntity() && collision.B != ((Component) component).getEntity()) continue;
                    if (Collision.HOLD == collision.state) {
                        if (((Component) component).getEntity() == collision.A) swapCollisionEntities(collision);
                        system.onCollision(collision);
                    }
                }
            }
        }
    }

    private void handleCollisionExit() {
        for (System system : systemManager.listOfSystemsForCollisionHandling) {
            for (Object component : system.getComponentList()) {
                system.setCurrentComponent((Component) component);
                for (Collision collision : systemManager.collisions) {
                    Logger.trace(String.format("Visiting exit collision [%s] for component [%s]", collision, component));
                    if (collision.A != ((Component) component).getEntity() && collision.B != ((Component) component).getEntity()) continue;
                    if (Collision.EXITED == collision.state) {
                        if (((Component) component).getEntity() == collision.A) swapCollisionEntities(collision);
                        system.onCollisionEnd(collision);
                    }
                }
            }
        }
    }

    private void swapCollisionEntities(Collision collision) {
        Collidable temp = collision.B;
        collision.B = collision.A;
        collision.A = temp;
    }

    @Override
    public void handleCollisions() {
        handleCollisionEnter();
        handleCollisionHold();
        handleCollisionExit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void render(Window window) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        float diffMillis;
        long nanosBufferSwap;
        long nanosRender = java.lang.System.nanoTime();

        for (System system : systemManager.listOfSystemsForRender) {
            for (Component component : (List<Component>) system.getComponentList()) {
                if (component.getState() >= AbstractComponent.READY_TO_OPERATE_STATE) {
                    Logger.trace(String.format(
                            "Handling render component [%s: %s]",
                            system.getClass().getName(),
                            component.getEntity().getName())
                    );
                    try {
                        system.setCurrentComponent(component);
                        system.render(window);
                    } catch (ComponentNotFoundException | NullPointerException e) {
                        Logger.error(e);
                        component.setState(AbstractComponent.READY_TO_INIT_STATE);
                    }
                } else if (component.getState() == AbstractComponent.READY_TO_INIT_STATE) {
                    component.setState(AbstractComponent.READY_TO_OPERATE_STATE);
                }
            }
        }

        diffMillis = (float) (java.lang.System.nanoTime() - nanosRender) / 1_000_000L;
        Logger.trace(String.format("Graphics systems handling ended! Spent time: <bold>%.3f[ms]</>", diffMillis));

        nanosBufferSwap = java.lang.System.nanoTime();
        glfwSwapBuffers(window.getWindow());
        diffMillis = (float) (java.lang.System.nanoTime() - nanosBufferSwap) / 1_000_000L;
        Logger.trace(String.format("Graphics buffer swap ended! Spent time: <bold>%.3f[ms]</>", diffMillis));
    }

}
