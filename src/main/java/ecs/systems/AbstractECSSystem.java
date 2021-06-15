package ecs.systems;

import ecs.components.ECSComponent;
import ecs.entities.Entity;
import ecs.managment.memory.Pool;
import ecs.managment.memory.IPool;
import ecs.math.Vector3f;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractECSSystem<E extends ECSComponent> implements ECSSystem<E> {
    public static final int INIT_MASK       = 0b0000_0001;
    public static final int UPDATE_MASK     = 0b0000_0010;
    public static final int RENDER_MASK     = 0b0000_0100;
    public static final int COLLISION_MASK  = 0b0000_1000;

    public E currentComponent;

    private int workflowMask = INIT_MASK & UPDATE_MASK & RENDER_MASK;

    private List<E> componentList = new LinkedList<>();
    private Set<E>  componentSet  = new HashSet<>();

    protected IPool<Vector3f> vector3IPool = new Pool<>(1000, Vector3f::new);


    /* -------------- Getters -------------- */

    @Override
    public List<E> getComponentList() {
        return componentList;
    }

    @Override
    public Set<E> componentSet() {
        return componentSet;
    }

    @Override
    public E getCurrentComponent() {
        return currentComponent;
    }

    @Override
    public int getWorkflowMask() {
        return workflowMask;
    }

    /* -------------- Setters -------------- */

    @Override
    public void componentList(List<E> componentList) {
        this.componentList = componentList;
    }

    @Override
    public void componentSet(Set<E> componentSet) {
        this.componentSet = componentSet;
    }

    @Override
    public void setCurrentComponent(E component) {
        this.currentComponent = component;
    }


    /* -------------- Other methods -------------- */

    @Override
    public void addComponent(E component) {
        if (componentSet.add(component)) {
            componentList.add(component);
        }
    }

    @Override
    public E getComponent(E that) {
        if (componentSet.contains(that)) {
            return componentList.stream().filter((c) -> that.getEntity().equals(c.getEntity())).collect(Collectors.toList()).get(0);
        }
        return null;
    }

    @Override
    public void removeComponent(E component) {
        if (componentSet.remove(component)) {
            componentList.remove(component);
        }
    }

    // TODO: 08.06.2021 this method must return an entity with specific name
    @Override
    public Entity getEntityByName(String name) {
        throw new UnsupportedOperationException();
    }

    /* -------------- Component API -------------- */

    @Override
    public void addComponent(Type type) throws IllegalArgumentException, ClassCastException {
        currentComponent.getEntity().addComponent(type);
    }

    @Override
    public <T extends ECSComponent> T getComponent(Type type) throws IllegalArgumentException, ClassCastException {
        return currentComponent.getEntity().getComponent(type);
    }

    @Override
    public <T extends ECSComponent> T removeComponent(Type type) throws IllegalArgumentException, ClassCastException {
        return currentComponent.getEntity().removeComponent(type);
    }
}
