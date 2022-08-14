package ecs.systems;

import ecs.components.ECSComponent;
import ecs.entities.Entity;
import ecs.managment.memory.Pool;
import ecs.managment.memory.IPool;
import vectors.Vector3f;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractECSSystem<E extends ECSComponent> implements ECSSystem<E> {
    public static final int INIT_MASK       = 1 << 0;
    public static final int UPDATE_MASK     = 1 << 1;
    public static final int RENDER_MASK     = 1 << 2;
    public static final int COLLISION_MASK  = 1 << 3;
    public static final int COLLISION_HANDLING_MASK  = 1 << 4;

    class ComponentSetStructure {
        public List<E> componentList = new LinkedList<>();
        public Set<E>  componentSet  = new HashSet<>();


        public void add(E component) {
            if (componentSet.add(component)) {
                componentList.add(component);
            }
        }

        public E get(E component) {
            if (componentSet.contains(component)) {
                return componentList.stream()
                        .filter(c -> component.getEntity().equals(c.getEntity()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Error while finding component! The component contains int component list but wasn't found!"));
            }
            return null;
        }

        public void remove(E component) {
            if (componentSet.remove(component)) {
                componentList.remove(component);
            }
        }
    }

    public ComponentSetStructure componentSetStructure = new ComponentSetStructure();

    public E component;

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
