package org.north.core.system;

import org.joml.Vector3f;
import org.north.core.architecture.entity.ComponentManager;
import org.north.core.architecture.entity.Entity;
import org.north.core.component.Camera;
import org.north.core.component.Component;
import org.north.core.context.ApplicationContext;
import org.north.core.exception.ComponentAlreadyExistsException;
import org.north.core.management.memory.Pool;
import org.north.core.management.memory.Vector3fPool;
import org.north.core.utils.logger.LoggerFactory;

import java.util.*;
import java.util.logging.Logger;

public abstract class AbstractSystem<E extends Component> implements System<E> {

    protected static final Logger log = LoggerFactory.createLogger(AbstractSystem.class);

    // map for storing componentId-to-component pair
    private final Map<UUID, E> componentMap = new HashMap<>();

    protected final Pool<Vector3f> vector3fPool;
    protected final ComponentManager cm;
    protected final Entity sceneRoot;

    public AbstractSystem(ApplicationContext context) {
        this.cm = context.getDependency(ComponentManager.class);
        this.sceneRoot = context.getDependency(Entity.class);
        this.vector3fPool = context.getDependency(Vector3fPool.class);
    }

    //todo: Very bad architecture decision, needs to refactor!
    // Maybe we should create a map of component class to
    // ComponentAttachmentListener in between layers of client api
    // and SystemManager, or even in SystemManager class.
    // In that case we can add listeners for any kind of component
    // and implement special logic to run in engine environment for that component
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
    public Collection<E> getComponentUnmodifiableCollection() {
        return Collections.unmodifiableCollection(componentMap.values());
    }

    @Override
    public final E getComponent(UUID componentId) {
//        if (component == null) {
//            throw new ComponentNotFoundException(componentId);
//        }
        return componentMap.get(componentId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final E addComponent(Component component)
            throws IllegalArgumentException, ClassCastException, ComponentAlreadyExistsException {
        if (componentMap.containsKey(component.getId())) {
            throw new ComponentAlreadyExistsException(component.getClass());
        }
        return componentMap.put(component.getId(), (E) component);
        // Logger.debug(String.format("Component added [id=%d type=%s]", component.getId(), component.getClass().getSimpleName()));
    }

    @Override
    public final E removeComponent(UUID componentId) {
        return componentMap.remove(componentId);
    }

    @Override
    public void reset() {
        componentMap.clear();
    }


}
