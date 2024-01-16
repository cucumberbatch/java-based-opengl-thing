package org.north.core.systems;

import org.north.core.architecture.ComponentManager;
import org.north.core.architecture.EntityManager;
import org.north.core.architecture.TreeEntityManager;
import org.north.core.components.Component;
import org.north.core.exception.ComponentAlreadyExistsException;
import org.north.core.exception.ComponentNotFoundException;
import org.north.core.managment.memory.Pool;
import org.north.core.managment.memory.IPool;
import org.north.core.utils.Logger;
import org.joml.Vector3f;

import java.util.*;

public abstract class AbstractSystem<E extends Component> implements System<E> {

    // map for storing componentId-to-component pair
    private Map<Long, E>  componentMap  = new HashMap<>();

    protected IPool<Vector3f> vector3IPool = new Pool<>(1000, Vector3f::new);

    public ComponentManager componentManager;
    public EntityManager entityManager;
    public E component;

    public AbstractSystem() {
        this.componentManager = ComponentManager.getInstance();
        this.entityManager = TreeEntityManager.getInstance();
    }

    @Override
    public Iterator<E> getComponentIterator() {
        return componentMap.values().iterator();
    }

    @Override
    public List<E> getComponentList() {
        return new ArrayList<>(componentMap.values());
    }

    @Override
    public E getComponent() {
        return component;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setCurrentComponent(Component component) {
        this.component = (E) component;
    }


    @Override
    @SuppressWarnings("unchecked")
    public void addComponent(Component component)
            throws IllegalArgumentException, ClassCastException, ComponentAlreadyExistsException {
        if (componentMap.containsKey(component.getId())) {
            throw new ComponentAlreadyExistsException(component.getClass());
        }
        componentMap.put(component.getId(), (E) component);
        Logger.debug(String.format("Component added [id=%d type=%s]", component.getId(), component.getClass().getSimpleName()));
    }

    @Override
    public E getComponent(long componentId) {
        E component = componentMap.get(componentId);
        if (component == null) {
            throw new ComponentNotFoundException(componentId);
        }
        return component;
    }

    @Override
    public E removeComponent(long componentId) {
        return componentMap.remove(componentId);
    }

}
