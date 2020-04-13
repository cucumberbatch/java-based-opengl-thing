package ecs.entities;

import ecs.Engine;
import ecs.Scene;
import ecs.components.Component;
import ecs.components.ComponentType;
import ecs.components.Transform;
import ecs.util.Instantiatable;
import ecs.util.Replicable;
import ecs.util.Turntable;
import ecs.util.Layer;

import java.util.*;

/**
 * Entity is an object that contains a bunch of components
 * that describes its nature
 *
 * @author cucumberbatch
 */
public class Entity implements IComponentManager, Turntable, Instantiatable<Entity>, Replicable<Entity> {
    private Engine engine;
    public Layer layer;
    public String tag;

    // Collection of daughters entities
    public List<Transform> daughters = new ArrayList<>();

    // Map of pairs of component types and components
    public Map<ComponentType, Component> componentMap = new HashMap<>();

    // Activity state of the entity
    public boolean activity;

    // Transform component of that entity
    public Transform transform;

    // Transform component of the parent entity
    public Transform parent;


    public Entity(Engine engine, Scene scene) {
        this(engine, scene, Layer.DEFAULT, "Default");
    }

    public Entity(
            Engine engine,
            Scene scene,
            Layer layer,
            String tag) {
        this.engine = engine;
        this.engine.setScene(scene);
        this.layer = layer;
        this.tag = tag;

        AddComponent(ComponentType.TRANSFORM);
        transform = GetComponent(ComponentType.TRANSFORM);

        /*
         Such a weird stuff right here...
         actually, its a necessary reference manipulation which will help you to get
         the access to a transform component from any component or even entity itself
        */
        transform.transform(transform);
    }

    /* Get the root entity in this branch */
    public Entity root() {
        Entity root = this;
        while (root.parent != null) {
            root = root.parent.entity();
        }
        return root;
    }

    /* Attach this entity to another entity */
    public void attachTo(Entity entity) {
        if (parent != null && parent.entity().daughters.size() > 0) {
            parent.entity().daughters.remove(this.transform);
        }
        parent = entity.transform;
        entity.daughters.add(transform);
    }

    /* Compare tag of this and other entity by entity */
    public int compareTag(Entity entity) {
        return compareTag(entity.tag);
    }

    /* Compare tag of this and other entity by string */
    public int compareTag(String tag) {
        return this.tag.compareTo(tag);
    }

    // ---------------------  Component access interface  ------------------------------------------------------

    @Override
    public void AddComponent(ComponentType type) throws IllegalArgumentException, ClassCastException {
        if (!componentMap.containsKey(type)) {
            Component component = initializedComponent(type);
            componentMap.put(type, component);
            engine.addComponentToSystem(type, component);
        }
    }

    @Override
    public <E extends Component> E GetComponent(ComponentType type) throws IllegalArgumentException, ClassCastException {
        return (E) componentMap.get(type);
    }

    @Override
    public <E extends Component> E RemoveComponent(ComponentType type) throws IllegalArgumentException, ClassCastException {
        return (E) engine.removeComponentFromSystem(type, componentMap.remove(type));
    }

    public <E extends Component> E initializedComponent(ComponentType type) {
        E component = engine.instantiateNewComponent(type);
        component.name(component.getClass().getSimpleName());
        component.transform(transform);
        component.entity(this);
        return component;
    }

    // ---------------------  Activity control interface  ------------------------------------------------------

    @Override
    public boolean isActive() {
        return activity;
    }

    @Override
    public void setActivity(boolean activity) {
        this.activity = activity;
    }

    @Override
    public void switchActivity() {
        activity = !activity;
    }

    @Override
    public Entity getInstance() {
        Entity clone = new Entity(engine, engine.getScene(), layer, tag);
        Set<Component> components = new HashSet<>();
        components.addAll(componentMap.values());
        clone.activity = activity;
        return null;
    }

    @Override
    public Entity getReplica() {
        return null;
    }
}
