package ecs.systems;

import ecs.components.ECSComponent;
import ecs.gl.Window;
import ecs.systems.processes.ISystem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static ecs.systems.AbstractECSSystem.COLLISION_MASK;
import static ecs.systems.AbstractECSSystem.INIT_MASK;
import static ecs.systems.AbstractECSSystem.RENDER_MASK;
import static ecs.systems.AbstractECSSystem.UPDATE_MASK;
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


    public boolean hasSystem(ECSSystem.Type type) {
        return systemMap.containsKey(type);
    }

    public void addSystem(ECSSystem.Type type, ECSSystem system) {
        if (systemMap.put(type, system) == null) attachToSystemLists(system);
    }

    // Attach system to
    private void attachToSystemLists(ECSSystem system) {
        int mask = system.getWorkflowMask();
        if ((mask & INIT_MASK) > 0)      listOfSystemsForInit     .add(system);
        if ((mask & UPDATE_MASK) > 0)    listOfSystemsForUpdate   .add(system);
        if ((mask & RENDER_MASK) > 0)    listOfSystemsForRender   .add(system);
        if ((mask & COLLISION_MASK) > 0) listOfSystemsForCollision.add(system);
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
            for (Object component : system.getComponentList()) {
                system.setComponent((ECSComponent) component);
                system.init();
            }
        }
    }

    @Override
    /* We are sure that each component object from system.getComponentList()
    is an ECSComponent type, because the only way of adding a component into
    component list is through component factory method call, that by definition
    generates objects of ECSComponent type */
    @SuppressWarnings("unchecked")
    public void update(float deltaTime) {
        glfwPollEvents();

        for (ECSSystem system : listOfSystemsForUpdate) {
            for (Object component : system.getComponentList()) {
                system.setComponent((ECSComponent) component);
                system.update(deltaTime);
            }
        }
    }

    @Override
    /* We are sure that each component object from system.getComponentList()
    is an ECSComponent type, because the only way of adding a component into
    component list is through component factory method call, that by definition
    generates objects of ECSComponent type */
    @SuppressWarnings("unchecked")
    public void render(Window window) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        for (ECSSystem system : listOfSystemsForRender) {
            for (Object component : system.getComponentList()) {
                system.setComponent((ECSComponent) component);
                system.render(window);
            }
        }

        glfwSwapBuffers(window.getWindow());
    }

    /* We are sure that each component object from system.getComponentList()
    is an ECSComponent type, because the only way of adding a component into
    component list is through component factory method call, that by definition
    generates objects of ECSComponent type */
    @SuppressWarnings("unchecked")
    public void collide() {
        for (ECSSystem system : listOfSystemsForCollision) {
            for (Object component : system.getComponentList()) {
                system.setComponent((ECSComponent) component);
//                system.collide(collision);
            }
        }
    }
}
