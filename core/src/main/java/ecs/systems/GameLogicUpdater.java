package ecs.systems;

import ecs.scene.Scene;
import ecs.scene.SceneInitializer;
import ecs.architecture.ComponentManager;
import ecs.architecture.EntityManager;
import ecs.components.AbstractComponent;
import ecs.components.Camera;
import ecs.components.Component;
import ecs.entities.Entity;
import ecs.exception.ComponentNotFoundException;
import ecs.graphics.Graphics;
import ecs.graphics.Window;
import ecs.managment.FrameTiming;
import ecs.managment.SystemManager;
import ecs.physics.Collidable;
import ecs.systems.processes.CollisionHandlingProcess;
import ecs.systems.processes.InitProcess;
import ecs.systems.processes.RenderProcess;
import ecs.systems.processes.UpdateProcess;
import ecs.utils.Logger;
import ecs.utils.Stopwatch;
import org.joml.Vector3f;

import java.util.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

//@SuppressWarnings("rawtypes")
public class GameLogicUpdater implements GameLogic {

    private Window window;
    private SceneInitializer sceneInitializer;
    private Scene scene;
    private Graphics graphics;

    private Camera camera;

    public GameLogicUpdater(Window window) {
        this.window = window;
        this.graphics = Graphics.getInstance(window);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public EntityManager    entityManager;
    public ComponentManager componentManager;
    public SystemManager    systemManager;

    @Override
    public void run() {
        Logger.info("Game loop started");

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

        Logger.info("Game loop ended");
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
    public void init() throws RuntimeException {
        // todo: needs to check for adding new components in runtime and checks for running out of components
        //  to initialize, so we can skip iteration moment in init() method call until the next component addition
        //
        //    if (systemManager.hasNoComponentsToInit()) return;
        //

        for (InitProcess process : systemManager.listOfSystemsForInit) {
            System<? extends Component> system = (System<? extends Component>) process;
            for (Component component : system.getComponentList()) {
                if (component.getState() == AbstractComponent.READY_TO_INIT_STATE) {
                    Logger.trace(String.format(
                            "Handling init component [%s: %s]",
                            system.getClass().getName(),
                            component.getEntity().getName())
                    );
                    try {
                        system.setCurrentComponent(component);
                        process.init();
                        component.setState(AbstractComponent.READY_TO_OPERATE_STATE);

                        if (component instanceof Camera) {
                            this.camera = (Camera) component;
                        }

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

    //todo: performance
    @Override
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

    //todo: performance
    @Override
    public void update(float deltaTime) {
        Stopwatch.start();
        glfwPollEvents();

        for (UpdateProcess process : systemManager.listOfSystemsForUpdate) {
            System<? extends Component> system = (System<? extends Component>) process;
            for (Component component : system.getComponentList()) {
                if (component.getState() == AbstractComponent.READY_TO_OPERATE_STATE) {
                    Logger.trace(String.format(
                            "Handling update component [%s: %s]",
                            system.getClass().getName(),
                            component.getEntity().getName())
                    );
                    try {
                        system.setCurrentComponent(component);
                        process.update(deltaTime);
                    } catch (ComponentNotFoundException | NullPointerException e) {
                        Logger.error(e);
                        component.setState(AbstractComponent.READY_TO_INIT_STATE);
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
            for (Component component : system.getComponentList()) {
                system.setCurrentComponent(component);
                for (Collision collision : systemManager.collisions) {
                    Logger.trace(String.format("Visiting enter collision [%s] for component [%s]", collision, component));
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
            for (Component component : system.getComponentList()) {
                system.setCurrentComponent(component);
                for (Collision collision : systemManager.collisions) {
                    Logger.trace(String.format("Visiting hold collision [%s] for component [%s]", collision, component));
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
            for (Component component : system.getComponentList()) {
                system.setCurrentComponent(component);
                for (Collision collision : systemManager.collisions) {
                    Logger.trace(String.format("Visiting exit collision [%s] for component [%s]", collision, component));
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

        for (RenderProcess process : systemManager.listOfSystemsForRender) {
            System<? extends Component> system = (System<? extends Component>) process;
            List<? extends Component> components = system.getComponentList();
            sortComponentsByDistanceToCamera(components);
            for (Component component : components) {
                if (component.getState() == AbstractComponent.READY_TO_OPERATE_STATE) {
                    Logger.trace(String.format(
                            "Handling render component [%s: %s]",
                            system.getClass().getName(),
                            component.getEntity().getName())
                    );
                    try {
                        system.setCurrentComponent(component);
                        process.render(graphics);
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

    private void sortComponentsByDistanceToCamera(List<? extends Component> components) {
        components.sort((o1, o2) -> {
            if (Objects.isNull(camera)) return 0;

            Vector3f cameraPosition = camera.getPosition();
            float o1Distance = o1.getTransform().position.distance(cameraPosition);
            float o2Distance = o2.getTransform().position.distance(cameraPosition);
            return (int) (o2Distance - o1Distance);
        } );
    }

}
