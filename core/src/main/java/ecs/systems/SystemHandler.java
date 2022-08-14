package ecs.systems;

import ecs.components.ECSComponent;
import ecs.entities.Entity;
import ecs.gl.Window;
import ecs.systems.processes.ISystem;
import ecs.utils.Logger;

import java.util.*;

import static ecs.systems.AbstractECSSystem.*;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

@SuppressWarnings("rawtypes")
public class SystemHandler implements ISystem {

    private final Map<ECSSystem.Type, ECSSystem> systemMap  = new HashMap<>();

    private final List<ECSSystem> systemList                = new LinkedList<>();

    private final List<ECSSystem> listOfSystemsForInit      = new LinkedList<>();
    private final List<ECSSystem> listOfSystemsForUpdate    = new LinkedList<>();
    private final List<ECSSystem> listOfSystemsForRender    = new LinkedList<>();
    private final List<ECSSystem> listOfSystemsForCollision = new LinkedList<>();
    private final List<ECSSystem> listOfSystemsForCollisionHandling = new LinkedList<>();

    private final List<Collision> collisions = new ArrayList<>();

    {
        Comparator<Collision> comparator = (that, other) -> (
                that.A == other.A && that.B == other.B ||
                that.A == other.B && that.B == other.A) ? 0 : -1;
    }

    public boolean hasSystem(ECSSystem.Type type) {
        return systemMap.containsKey(type);
    }

    public void addSystem(ECSSystem.Type type, ECSSystem system) {
        if (systemMap.put(type, system) == null) attachToSystemLists(system);
    }

    // Attach system to
    private void attachToSystemLists(ECSSystem system) {
        int mask = system.getWorkflowMask();
        if ((mask & INIT_MASK) > 0)               listOfSystemsForInit.add(system);
        if ((mask & UPDATE_MASK) > 0)             listOfSystemsForUpdate.add(system);
        if ((mask & RENDER_MASK) > 0)             listOfSystemsForRender.add(system);
        if ((mask & COLLISION_MASK) > 0)          listOfSystemsForCollision.add(system);
        if ((mask & COLLISION_HANDLING_MASK) > 0) listOfSystemsForCollisionHandling.add(system);
    }

    public ECSSystem getSystem(ECSSystem.Type type) {
        return systemMap.get(type);
    }


    public void linkComponentAndSystem(ECSSystem.Type type, ECSComponent component) {
        ECSSystem system = systemMap.get(type);
        system.addComponent(component);
        component.setSystem(system);
    }

    public void removeComponent(ECSSystem.Type type, ECSComponent component) {
        systemMap.get(type).removeComponent(component);
    }

    @Override
    /* We are sure that each component object from system.getComponentList()
    is an ECSComponent type, because the only way of adding a component into
    component list is through component factory method call, that by definition
    generates objects of ECSComponent type */
    @SuppressWarnings("unchecked")
    public void init() throws Exception {
        for (ECSSystem system : listOfSystemsForInit) {
            Logger.trace(String.format("Handling init system [%s]", system.getClass().getName()));
            for (Object component : system.getComponentList()) {
                system.setComponent((ECSComponent) component);
                system.init();
            }
        }
    }

    public void preUpdate() {
        if (listOfSystemsForCollision.isEmpty()) return;

        List<MeshCollider> componentList;
        CollisionPair pair;
        Collision collision;
        Collision previousFrameCollision;
        MeshCollider that;
        MeshCollider other;
        Entity A;
        Entity B;
        byte collisionState;
        int componentListSize;
        int collisionsListSize;
        boolean isCollisionFound;
        int lindex;
        int rindex;
        int cindex;


        collisionsListSize = collisions.size();

        for (ECSSystem system : listOfSystemsForCollision) {
            componentList = system.getComponentList();
            componentListSize = componentList.size();
            for (lindex = 0; lindex < componentListSize - 1; lindex++) {
                that = componentList.get(lindex);
                for (rindex = lindex + 1; rindex < componentListSize; rindex++) {
                    other = componentList.get(rindex);
                    if (that.mesh == null || other.mesh == null) return;
                    if (that.mesh.isIntersects(other.mesh)) {
                        A = that.entity;
                        B = other.entity;
                        isCollisionFound = false;
                        for (cindex = 0; cindex < collisionsListSize; cindex++) {
                            previousFrameCollision = collisions.get(cindex);
                            if (previousFrameCollision.A == A && previousFrameCollision.B == B ||
                                previousFrameCollision.A == B && previousFrameCollision.B == A) {
                                isCollisionFound = true;
                                if (Collision.ENTERED == previousFrameCollision.state) {
                                    previousFrameCollision.pair = new CollisionPair(A.transform.position, B.transform.position);
                                    previousFrameCollision.state = Collision.HOLD;
                                    previousFrameCollision.isModified = true;
                                    Logger.info(String.format("Collision modified! a1: %s\tstate: %s", previousFrameCollision, previousFrameCollision.state));
                                }
                                break;
                            }
                        }
                        if (!isCollisionFound) {
                            collisionState = Collision.ENTERED;
                            pair = new CollisionPair(A.transform.position, B.transform.position);
                            collision = new Collision(A, B, pair, collisionState);
                            collision.isModified = true;
                            collisions.add(collision);
                            collisionsListSize++;
                            Logger.info(String.format("Collision added! a1: %s\tstate: %s", collision, collision.state));
                        }
                    } else {
                        A = that.entity;
                        B = other.entity;
                        for (cindex = 0; cindex < collisionsListSize; cindex++) {
                            previousFrameCollision = collisions.get(cindex);
                            if (previousFrameCollision.A == A && previousFrameCollision.B == B ||
                                previousFrameCollision.A == B && previousFrameCollision.B == A) {
                                if (Collision.EXITED != previousFrameCollision.state) {
                                    previousFrameCollision.isModified = true;
                                    previousFrameCollision.state = Collision.EXITED;
                                    Logger.info(String.format("Collision modified! a1: %s\tstate: %s", previousFrameCollision, previousFrameCollision.state));
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
            collision = collisions.get(cindex);
            if (Collision.EXITED == collision.state && !collision.isModified) {
                collisions.remove(cindex);
                collisionsListSize--;
                Logger.info(String.format("Collision removed! a1: %s\tstate: %s", collision, collision.state));
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(float deltaTime) {
        long nanos = System.nanoTime();
        glfwPollEvents();

        for (ECSSystem system : listOfSystemsForUpdate) {
            Logger.trace(String.format("Handling update system [%s]", system.getClass().getName()));
            for (Object component : system.getComponentList()) {
                system.setComponent((ECSComponent) component);
                system.update(deltaTime);
            }
        }

        float diffMillis = (float) (System.nanoTime() - nanos) / 1_000_000L;
        Logger.trace(String.format("Update systems handling ended! Spent time: <bold>%.3f[ms]</>", diffMillis));
    }

    public void handleCollisionEnter() {
        for (ECSSystem system : listOfSystemsForCollisionHandling) {
            for (Object component : system.getComponentList()) {
                system.setComponent((ECSComponent) component);
                for (Collision collision : collisions) {
                    if (Collision.ENTERED == collision.state)
                        system.onCollisionStart(collision);
                }
            }
        }
    }

    public void handleCollisionHold() {
        for (ECSSystem system : listOfSystemsForCollisionHandling) {
            for (Object component : system.getComponentList()) {
                system.setComponent((ECSComponent) component);
                for (Collision collision : collisions) {
                    if (Collision.HOLD == collision.state)
                        system.onCollision(collision);
                }
            }
        }
    }

    public void handleCollisionExit() {
        for (ECSSystem system : listOfSystemsForCollisionHandling) {
            for (Object component : system.getComponentList()) {
                system.setComponent((ECSComponent) component);
                for (Collision collision : collisions) {
                    if (Collision.EXITED == collision.state)
                        system.onCollisionEnd(collision);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void render(Window window) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        float diffMillis;
        long nanosBufferSwap;
        long nanosRender = System.nanoTime();

        for (ECSSystem system : listOfSystemsForRender) {
            Logger.trace(String.format("Handling render system [%s]", system.getClass().getName()));
            for (Object component : system.getComponentList()) {
                system.setComponent((ECSComponent) component);
                system.render(window);
            }
        }

        diffMillis = (float) (System.nanoTime() - nanosRender) / 1_000_000L;
        Logger.trace(String.format("Graphics systems handling ended! Spent time: <bold>%.3f[ms]</>", diffMillis));

        nanosBufferSwap = System.nanoTime();
        glfwSwapBuffers(window.getWindow());
        diffMillis = (float) (System.nanoTime() - nanosBufferSwap) / 1_000_000L;
        Logger.trace(String.format("Graphics buffer swap ended! Spent time: <bold>%.3f[ms]</>", diffMillis));
    }

}
