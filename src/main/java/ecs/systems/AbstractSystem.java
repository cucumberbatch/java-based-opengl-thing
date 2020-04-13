package ecs.systems;

import ecs.components.Component;
import ecs.components.ComponentType;
import ecs.util.managment.memory.AbstractPool;
import ecs.util.managment.memory.Pool;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractSystem implements System {
    private List<Component> component_list = new LinkedList<>();
    private Set<Component> component_set = new HashSet<>();

    public Component current_component;

    protected Pool<Vector3f> vector3Pool = new AbstractPool<Vector3f>() {
        @Override
        public Vector3f create() {
            return new Vector3f();
        }
    };


    /* -------------- Getters -------------- */

    @Override
    public List<Component> componentList() {
        return component_list;
    }

    @Override
    public Set<Component> componentSet() {
        return component_set;
    }

    @Override
    public <E extends Component> E currentComponent() {
        return (E) current_component;
    }

    @Override
    public Component getComponent(Component that) {
        if (component_set.contains(that)) {
            return component_list.stream().filter((other) -> that.entity().equals(other.entity())).collect(Collectors.toList()).get(0);
        }
        return null;
    }


    /* -------------- Setters -------------- */

    @Override
    public void componentList(List<Component> componentList) {
        this.component_list = componentList;
    }

    @Override
    public void componentSet(Set<Component> componentSet) {
        this.component_set = componentSet;
    }

    @Override
    public void currentComponent(Component component) {
        this.current_component = component;
    }

    @Override
    public void addComponent(Component component) {
        if (component_set.add(component)) {
            component_list.add(component);
        }
    }


    /* -------------- Other methods -------------- */

    @Override
    public void removeComponent(Component component) {
        if (component_set.remove(component)) {
            component_list.remove(component);
        }
    }

    @Override
    public void AddComponent(ComponentType type) throws IllegalArgumentException, ClassCastException {
        current_component.entity().AddComponent(type);
    }

    @Override
    public <E extends Component> E GetComponent(ComponentType type) throws IllegalArgumentException, ClassCastException {
        return (E) current_component.entity().GetComponent(type);
    }

    @Override
    public <E extends Component> E RemoveComponent(ComponentType type) throws IllegalArgumentException, ClassCastException {
        return current_component.entity().RemoveComponent(type);
    }
}
