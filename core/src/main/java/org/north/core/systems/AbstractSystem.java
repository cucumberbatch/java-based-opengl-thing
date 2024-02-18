package org.north.core.systems;

import org.north.core.architecture.EntityManager;
import org.north.core.architecture.entity.ComponentManager;
import org.north.core.components.Camera;
import org.north.core.components.Component;
import org.north.core.context.ApplicationContext;
import org.north.core.exception.ComponentAlreadyExistsException;
import org.north.core.exception.ComponentNotFoundException;
import org.north.core.managment.memory.Vector3fPool;
import org.north.core.managment.memory.Pool;
import org.joml.Vector3f;

import java.util.*;

public abstract class AbstractSystem<E extends Component> implements System<E> {

    // map for storing componentId-to-component pair
    private final Map<UUID, E> componentMap = new HashMap<>();

    protected final Pool<Vector3f> vector3fPool;
    protected final ComponentManager cm;
    protected final EntityManager em;

    public E component;

    public AbstractSystem(ApplicationContext context) {
        this.cm = context.getComponentManager();
        this.em = context.getEntityManager();
        this.vector3fPool = context.getDependency(Vector3fPool.class);
    }

    public final void setCameraComponent(Camera camera) {
        cm.setCameraComponent(camera);
    }

    @Override
    public final Iterator<E> getComponentIterator() {
        return componentMap.values().iterator();
    }

    @Override
    public final List<E> getComponentList() {
        return new ArrayList<>(componentMap.values());
    }

    @Override
    public final E getComponent() {
        return component;
    }

    @Override
    public final E getComponent(UUID componentId) {
        E component = componentMap.get(componentId);
        if (component == null) {
            throw new ComponentNotFoundException(componentId);
        }
        return component;
    }

    @Override
    public final <C extends Component> C getComponent(Class<C> componentClass) {
        return component.getEntity().getComponent(componentClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final void setCurrentComponent(Component component) {
        this.component = (E) component;
    }


    @Override
    @SuppressWarnings("unchecked")
    public final void addComponent(Component component)
            throws IllegalArgumentException, ClassCastException, ComponentAlreadyExistsException {
        if (componentMap.containsKey(component.getId())) {
            throw new ComponentAlreadyExistsException(component.getClass());
        }
        componentMap.put(component.getId(), (E) component);
        // Logger.debug(String.format("Component added [id=%d type=%s]", component.getId(), component.getClass().getSimpleName()));
    }

    @Override
    public final E removeComponent(UUID componentId) {
        return componentMap.remove(componentId);
    }

    @Override
    public final E removeComponent(Class<E> componentClass) {
        return null;//componentMap.
    }


}
