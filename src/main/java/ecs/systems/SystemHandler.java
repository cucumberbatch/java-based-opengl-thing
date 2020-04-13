package ecs.systems;

import ecs.components.Component;
import ecs.components.ComponentType;
import ecs.systems.processes.ISystem;
import ecs.systems.System;

import java.util.*;

public class SystemHandler implements ISystem {
    private Map<ComponentType, System> s_map = new HashMap<>();
    private List<System> s_list = new LinkedList<>();


    public boolean hasSystem(ComponentType type) {
        return s_map.containsKey(type);
    }

    public void addSystem(ComponentType type, System system) {
        if (s_map.put(type, system) == null) s_list.add(system);
    }

    public System getSystem(ComponentType type) {
        return s_map.get(type);
    }


    public void linkComponentAndSystem(ComponentType type, Component component) {
        System system = s_map.get(type);
        system.addComponent(component);
        component.system(system);
    }

    public void removeComponent(ComponentType type, Component component) {
        s_map.get(type).removeComponent(component);
    }

    @Override
    public void update(float deltaTime) {
        for (System system : s_list) {
            for (Component component : system.componentList()) {
                system.currentComponent(component);
                system.update(deltaTime);
            }
        }
    }
}
