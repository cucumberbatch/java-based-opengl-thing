package ecs.systems;

import ecs.GameLogic;
import ecs.Scene;
import ecs.SceneInitializer;
import ecs.architecture.ComponentManager;
import ecs.architecture.EntityManager;
import ecs.components.AbstractComponent;
import ecs.components.Component;
import ecs.components.Transform;
import ecs.entities.Entity;
import ecs.exception.ComponentNotFoundException;
import ecs.graphics.Graphics;
import ecs.graphics.Window;
import ecs.managment.FrameTiming;
import ecs.managment.SystemManager;
import ecs.physics.Collidable;
import ecs.utils.Logger;
import ecs.utils.Stopwatch;
import vectors.Vector3f;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

//@SuppressWarnings("rawtypes")
public class GameLogicUpdater implements GameLogic {

    private Window window;
    private SceneInitializer sceneInitializer;
    private Scene scene;
    private Graphics graphics;

    public GameLogicUpdater(Window window) {
        this.window = window;
        this.graphics = new Graphics(window);
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
//    @SuppressWarnings("unchecked")
    public void init() throws RuntimeException {
        for (System<? extends Component> system : systemManager.listOfSystemsForInit) {
            for (Component component : system.getComponentList()) {
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
        Stopwatch.start();

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
                    if (that.isStatic && other.isStatic) continue;
                    if (that.mesh == null || other.mesh == null) continue;
                    if (that.mesh.isIntersects(other.mesh)) {
                        positionA = componentManager.getComponent(A, Transform.class).position;
                        positionB = componentManager.getComponent(B, Transform.class).position;
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
                            //todo: add collision pool
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

        Stopwatch.stop("Collision register systems handling ended!");
    }

    private boolean isSameCollisionAsInPreviousFrame(Collision previousFrameCollision, Collidable A, Collidable B) {
        return (previousFrameCollision.A == A && previousFrameCollision.B == B) ||
               (previousFrameCollision.A == B && previousFrameCollision.B == A);
    }

    @Override
    public void update(float deltaTime) {
        Stopwatch.start();
        glfwPollEvents();

        for (System<? extends Component> system : systemManager.listOfSystemsForUpdate) {
            for (Component component : system.getComponentList()) {
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

        Stopwatch.stop("Update systems handling ended!");
    }

    // @Performance
    private void handleCollisionEnter() {
        Stopwatch.start();

        for (System<? extends Component> system : systemManager.listOfSystemsForCollisionHandling) {
            for (Component component : system.getComponentList()) {
                system.setCurrentComponent(component);
                for (Collision collision : systemManager.collisions) {
                    Logger.trace(String.format("Visiting enter collision [%s] for component [%s]", collision, component));
                    if (collision.A != (component).getEntity() && collision.B != component.getEntity()) continue;
                    if (Collision.ENTERED == collision.state) {
                        if (component.getEntity() == collision.A) swapCollisionEntities(collision);
                        system.onCollisionStart(collision);
                    }
                }
            }
        }

        Stopwatch.stop("Collision enter handling ended!");
    }

    // @Performance
    private void handleCollisionHold() {
        Stopwatch.start();

        for (System<? extends Component> system : systemManager.listOfSystemsForCollisionHandling) {
            for (Component component : system.getComponentList()) {
                system.setCurrentComponent(component);
                for (Collision collision : systemManager.collisions) {
                    Logger.trace(String.format("Visiting hold collision [%s] for component [%s]", collision, component));
                    if (collision.A != component.getEntity() && collision.B != component.getEntity()) continue;
                    if (Collision.HOLD == collision.state) {
                        if (component.getEntity() == collision.A) swapCollisionEntities(collision);
                        system.onCollision(collision);
                    }
                }
            }
        }

        Stopwatch.stop("Collision hold handling ended!");
    }

    // @Performance
    private void handleCollisionExit() {
        Stopwatch.start();

        for (System<? extends Component> system : systemManager.listOfSystemsForCollisionHandling) {
            for (Component component : system.getComponentList()) {
                system.setCurrentComponent(component);
                for (Collision collision : systemManager.collisions) {
                    Logger.trace(String.format("Visiting exit collision [%s] for component [%s]", collision, component));
                    if (collision.A != component.getEntity() && collision.B != component.getEntity()) continue;
                    if (Collision.EXITED == collision.state) {
                        if (component.getEntity() == collision.A) swapCollisionEntities(collision);
                        system.onCollisionEnd(collision);
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

    @Override
    public void handleCollisions() {
        handleCollisionEnter();
        handleCollisionHold();
        handleCollisionExit();
    }

    @Override
    public void render(Window window) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Stopwatch.start();

        for (System<? extends Component> system : systemManager.listOfSystemsForRender) {
            for (Component component : system.getComponentList()) {
                if (component.getState() == AbstractComponent.READY_TO_OPERATE_STATE) {
                    Logger.trace(String.format(
                            "Handling render component [%s: %s]",
                            system.getClass().getName(),
                            component.getEntity().getName())
                    );
                    try {
                        system.setCurrentComponent(component);
                        system.render(graphics);
                    } catch (ComponentNotFoundException | NullPointerException e) {
                        Logger.error(e);
                        component.setState(AbstractComponent.READY_TO_INIT_STATE);
                    }
                }
            }
        }

        Stopwatch.stop("Graphics systems handling ended!");

        Stopwatch.start();
        glfwSwapBuffers(window.getWindow());
        Stopwatch.stop("Graphics buffer swap ended!");
    }

}
