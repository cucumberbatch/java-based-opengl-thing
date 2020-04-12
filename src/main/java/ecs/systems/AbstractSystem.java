package ecs.systems;

import ecs.Engine;
import ecs.components.Component;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractSystem implements ISystem {
    public Engine engine;

    public List<Component> component_list = new LinkedList<>();
    public Set<Component> component_set = new HashSet<>();
    public Component current_component;


    public void addComponent(Component component) {
        if (component_set.add(component)) {
            component_list.add(component);
        }
    }

    public Component getComponent(Component component) {
        if (component_set.contains(component)) {
            return component_list.stream().filter(component.entity::equals).collect(Collectors.toList()).get(0);
        }
        return null;
    }

    public void removeComponent(Component component) {
        if (component_set.remove(component)) {
            component_list.remove(component);
        }
    }
}
