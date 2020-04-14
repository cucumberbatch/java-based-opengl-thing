package ecs.systems;

import ecs.components.Component;
import ecs.systems.processes.ISystem;

import java.util.*;

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
    public void update(float deltaTime) {
        for (System system : s_list) {
            for (Object component : system.componentList()) {
                system.currentComponent((Component) component);
                system.update(deltaTime);
            }
        }
    }
}
