package ecs.systems;

import ecs.components.Component;
import ecs.managment.memory.ConcretePool;
import ecs.managment.memory.Pool;
import ecs.math.Vector3f;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractSystem<E extends Component> implements System<E> {
    private List<E> component_list = new LinkedList<>();
    private Set<E> component_set = new HashSet<>();

    public E current_component;

    protected Pool<Vector3f> vector3Pool = new ConcretePool<>(1000, Vector3f::new);


    /* -------------- Getters -------------- */

    @Override
    public List<E> componentList() {
        return component_list;
    }

    @Override
    public Set<E> componentSet() {
        return component_set;
    }

    @Override
    public E currentComponent() {
        return current_component;
    }


    /* -------------- Setters -------------- */

    @Override
    public void componentList(List<E> componentList) {
        this.component_list = componentList;
    }

    @Override
    public void componentSet(Set<E> componentSet) {
        this.component_set = componentSet;
    }

    @Override
    public void currentComponent(E component) {
        this.current_component = component;
    }


    /* -------------- Other methods -------------- */

    @Override
    public void addComponent(E component) {
        if (component_set.add(component)) {
            component_list.add(component);
        }
    }

    @Override
    public E getComponent(E that) {
        if (component_set.contains(that)) {
            return component_list.stream().filter((c) -> that.entity().equals(c.entity())).collect(Collectors.toList()).get(0);
        }
        return null;
    }

    @Override
    public void removeComponent(E component) {
        if (component_set.remove(component)) {
            component_list.remove(component);
        }
    }


    /* -------------- Component API -------------- */

    @Override
    public void addComponent(Type type) throws IllegalArgumentException, ClassCastException {
        current_component.entity().addComponent(type);
    }

    @Override
    public <T extends Component> T getComponent(Type type) throws IllegalArgumentException, ClassCastException {
        return current_component.entity().getComponent(type);
    }

    @Override
    public <T extends Component> T removeComponent(Type type) throws IllegalArgumentException, ClassCastException {
        return current_component.entity().removeComponent(type);
    }
}
