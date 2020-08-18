package ecs.systems;

import ecs.components.Component;
import ecs.gl.Window;
import ecs.systems.processes.ISystem;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("rawtypes")
public class SystemHandler implements ISystem {

    private Map<System.Type, System> s_map = new HashMap<>();

    private List<System> s_list = new LinkedList<>();


    public boolean hasSystem(System.Type type) {
        return s_map.containsKey(type);
    }

    public void addSystem(System.Type type, System system) {
        if (s_map.put(type, system) == null) s_list.add(system);
    }

    public System getSystem(System.Type type) {
        return s_map.get(type);
    }


    public void linkComponentAndSystem(System.Type type, Component component) {
        System system = s_map.get(type);
        system.addComponent(component);
        component.system(system);
    }

    public void removeComponent(System.Type type, Component component) {
        s_map.get(type).removeComponent(component);
    }

    @Override
    public void init() throws Exception {
        createCapabilities();
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        for (System system : s_list) {
            for (Object component : system.componentList()) {
                system.currentComponent((Component) component);
                system.init();
            }
        }
    }

    @Override
    public void update(float deltaTime) {

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();

        for (System system : s_list) {
            for (Object component : system.componentList()) {
                system.currentComponent((Component) component);
                system.update(deltaTime);
            }
        }
    }

    @Override
    public void render(Window window) {
         glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        for (System system : s_list) {
            for (Object component : system.componentList()) {
                system.currentComponent((Component) component);
                system.render(window);
            }
        }

        glfwSwapBuffers(window.getWindow()); // swap the color buffers


    }
}
