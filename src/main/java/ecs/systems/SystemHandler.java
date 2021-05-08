package ecs.systems;

import ecs.components.Component;
import ecs.gl.Window;
import ecs.systems.processes.ISystem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static ecs.systems.AbstractSystem.*;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("rawtypes")
public class SystemHandler implements ISystem {

    private final Map<System.Type, System> systemMap     = new HashMap<>();

    private final List<System> systemList                = new LinkedList<>();

    private final List<System> listOfSystemsForInit      = new LinkedList<>();
    private final List<System> listOfSystemsForUpdate    = new LinkedList<>();
    private final List<System> listOfSystemsForRender    = new LinkedList<>();
    private final List<System> listOfSystemsForCollision = new LinkedList<>();


    public boolean hasSystem(System.Type type) {
        return systemMap.containsKey(type);
    }

    public void addSystem(System.Type type, System system) {
        if (systemMap.put(type, system) == null) attachToSystemLists(system);
    }

    // Attach system to
    private void attachToSystemLists(System system) {
        int mask = system.getWorkflowMask();
        if ((mask & INIT_MASK) > 0)      listOfSystemsForInit     .add(system);
        if ((mask & UPDATE_MASK) > 0)    listOfSystemsForUpdate   .add(system);
        if ((mask & RENDER_MASK) > 0)    listOfSystemsForRender   .add(system);
        if ((mask & COLLISION_MASK) > 0) listOfSystemsForCollision.add(system);
    }

    public System getSystem(System.Type type) {
        return systemMap.get(type);
    }


    public void linkComponentAndSystem(System.Type type, Component component) {
        System system = systemMap.get(type);
        system.addComponent(component);
        component.system(system);
    }

    public void removeComponent(System.Type type, Component component) {
        systemMap.get(type).removeComponent(component);
    }

    @Override
    public void init() throws Exception {
        for (System system : listOfSystemsForInit) {
            for (Object component : system.componentList()) {
                system.currentComponent((Component) component);
                system.init();
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        glfwPollEvents();

        for (System system : listOfSystemsForUpdate) {
            for (Object component : system.componentList()) {
                system.currentComponent((Component) component);
                system.update(deltaTime);
            }
        }
    }

    @Override
    public void render(Window window) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        for (System system : listOfSystemsForRender) {
            for (Object component : system.componentList()) {
                system.currentComponent((Component) component);
                system.render(window);
            }
        }

        glfwSwapBuffers(window.getWindow());
    }

    public void collide() {
        for (System system : listOfSystemsForCollision) {
            for (Object component : system.componentList()) {
                system.currentComponent((Component) component);
//                system.collide(collision);
            }
        }
    }
}
