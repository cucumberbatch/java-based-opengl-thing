package ecs.systems;

import ecs.components.ECSComponent;
import ecs.entities.Entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractECSSystem<E extends ECSComponent> implements ECSSystem<E> {
    public static final int INIT_MASK        = 0x1;
    public static final int UPDATE_MASK      = 0x2;
    public static final int RENDER_MASK      = 0x4;
    public static final int COLLISION_MASK   = 0x8;

    public static final int DESTRUCTION_MASK = 0x8000_0000;

    public E component;

    protected int workflowMask = INIT_MASK & UPDATE_MASK & RENDER_MASK;

    private List<E> componentList = new LinkedList<>();
    private Set<E>  componentSet  = new HashSet<>();


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
    public E getComponent() {
        return component;
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
    public void setComponent(E component) {
        this.component = component;
    }

    @Override
    public void setWorkflowMask(int mask) {
        this.workflowMask = mask;
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
        component.getEntity().addComponent(type);
    }

    @Override
    public <T extends ECSComponent> T getComponent(Type type) throws IllegalArgumentException, ClassCastException {
        return component.getEntity().getComponent(type);
    }

    @Override
    public <T extends ECSComponent> T removeComponent(Type type) throws IllegalArgumentException, ClassCastException {
        return component.getEntity().removeComponent(type);
    }
}
