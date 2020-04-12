package ecs;

import ecs.components.Component;
import ecs.components.ComponentType;
import ecs.systems.ISystem;
import ecs.systems.RigidBodySystem;
import ecs.systems.System;
import ecs.systems.TransformSystem;

import java.util.*;

public class SystemHandler implements ISystem {
    public Map<ComponentType, System> s_map = new HashMap<>();
    public List<System> s_list = new LinkedList<>();

    public SystemHandler() {
        s_map.put(ComponentType.TRANSFORM, new TransformSystem());
        s_map.put(ComponentType.RIGIDBODY, new RigidBodySystem());

        s_list.addAll(s_map.values());
    }


    public void linkComponentAndSystem(ComponentType type, Component component) {
        System system = s_map.get(type);
        system.addComponent(component);
        component.system = system;
    }

    public System getSystemByComponentType(ComponentType type) {
        return s_map.get(type);
    }

    public void removeComponent(ComponentType type, Component component) {
        s_map.get(type).removeComponent(component);
    }

    @Override
    public void update(float deltaTime) {
        for (System system : s_list) {
            for (Component component : system.component_list) {
                system.current_component = component;
                system.update(deltaTime);
            }
        }
    }
}
