package org.north.core.system;

import org.north.core.architecture.entity.ComponentContainer;
import org.north.core.architecture.entity.ComponentManager;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.joml.Vector3f;
import org.north.core.architecture.entity.Entity;
import org.north.core.component.Component;
import org.north.core.context.ApplicationContext;
import org.north.core.exception.ComponentAlreadyExistsException;
import org.north.core.management.memory.Pool;
import org.north.core.management.memory.Vector3fPool;

import java.util.*;

public abstract class AbstractSystem<C extends Component> implements System<C> {

    protected static final Logger log = LoggerFactory.getLogger(AbstractSystem.class);

    private final Class<C>       componentType;
    private final Pool<Vector3f> vector3fPool;

    protected final ComponentContainer container;
    protected final ComponentManager   cm;
    protected final Entity             sceneRoot;

    public AbstractSystem(Class<C> componentType, ApplicationContext context) {
        this.componentType = componentType;
        this.container     = context.getDependency(ComponentContainer.class);
        this.cm            = context.getDependency(ComponentManager.class);
        this.sceneRoot     = context.getDependency(Entity.class);
        this.vector3fPool  = context.getDependency(Vector3fPool.class);
    }

    @Override
    public final Iterator<C> getComponentIterator() {
        return container.getComponentsByType(componentType).iterator();
    }

    @Override
    public final List<C> getComponentList() {
        return new ArrayList<>(container.getComponentsByType(componentType));
    }

    @Override
    public Collection<C> getComponentUnmodifiableCollection() {
        return Collections.unmodifiableCollection(container.getComponentsByType(componentType));
    }

    @Override
    @SuppressWarnings("unchecked")
    public final C addComponent(Component component)
            throws IllegalArgumentException, ClassCastException, ComponentAlreadyExistsException {
        Entity entity = component.getEntity();
        if (container.has(entity, componentType)) {
            throw new ComponentAlreadyExistsException(component.getClass());
        }
        return (C) component;
        // Logger.debug(String.format("Component added [id=%d type=%s]", component.getId(), component.getClass().getSimpleName()));
    }

    @Override
    public final C removeComponent(Component component) {
        return container.remove(component.getEntity(), componentType);
    }

    @Override
    public void reset() {
        return;
    }

    protected Vector3f vec3f() {
        return vector3fPool.get();
    }

    protected void putBack(Vector3f vector) {
        vector3fPool.put(vector);
    }


}
