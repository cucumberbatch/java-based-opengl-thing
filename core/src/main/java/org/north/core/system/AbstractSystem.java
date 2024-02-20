package org.north.core.system;

import org.north.core.architecture.entity.Entity;
import org.north.core.architecture.tree.Tree;
import org.north.core.architecture.entity.ComponentManager;
import org.north.core.component.Camera;
import org.north.core.component.Component;
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
    protected final Tree<Entity> em;

    public AbstractSystem(ApplicationContext context) {
        this.cm = context.getComponentManager();
        this.em = context.getEntityTree();
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
    public final E getComponent(UUID componentId) {
        E component = componentMap.get(componentId);
        if (component == null) {
            throw new ComponentNotFoundException(componentId);
        }
        return component;
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
