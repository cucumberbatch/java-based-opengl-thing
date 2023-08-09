package ecs.systems;

import ecs.MainThread;
import ecs.architecture.ComponentManager;
import ecs.architecture.EntityManager;
import ecs.architecture.TreeEntityManager;
import ecs.components.Component;
import ecs.exception.ComponentAlreadyExistsException;
import ecs.exception.ComponentNotFoundException;
import ecs.managment.memory.Pool;
import ecs.managment.memory.IPool;
import ecs.utils.Logger;
import vectors.Vector3f;

import java.util.*;

public abstract class AbstractSystem<E extends Component> implements System<E> {
    public static final int INIT_MASK                = 1 << 0;
    public static final int UPDATE_MASK              = 1 << 1;
    public static final int RENDER_MASK              = 1 << 2;
    public static final int COLLISION_MASK           = 1 << 3;
    public static final int COLLISION_HANDLING_MASK  = 1 << 4;

    private int workflowMask = INIT_MASK & UPDATE_MASK & RENDER_MASK;

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

    // a very expensive method that creates linked list with copy of set of components
    @Override
    public List<E> getComponentList() {
        return new LinkedList<>(componentMap.values());
    }

    @Override
    public E getComponent() {
        return component;
    }

    @Override
    public int getWorkflowMask() {
        return workflowMask;
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
