package ecs.entities;

import ecs.Engine;
import ecs.Scene;
import ecs.components.Component;
import ecs.components.Transform;
import ecs.systems.System;
import ecs.utils.Instantiatable;
import ecs.utils.Layer;
import ecs.utils.Replicable;
import ecs.utils.Turntable;

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
    public UUID id;

    // Collection of daughter entities
    public List<Transform> daughters = new ArrayList<>();

    // Map of pairs of component types and components
    public Map<System.Type, Component> componentMap = new HashMap<>();

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
        this.id = UUID.randomUUID();

        addComponent(System.Type.TRANSFORM);
        transform = getComponent(System.Type.TRANSFORM);

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
        parent = transform.parent = entity.transform;
        entity.daughters.add(transform);
    }

    public void detachDaughters() {
        for (Transform daughter : daughters) {
            daughter.parent = this.parent;
        }

        daughters.clear();
    }

    public void detachComponents() {
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
    public void addComponent(System.Type type) throws IllegalArgumentException, ClassCastException {
        if (!componentMap.containsKey(type)) {
            Component component = initializeComponent(type);
            componentMap.put(type, component);
            engine.addComponentToSystem(type, component);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public <E extends Component> E getComponent(System.Type type) throws IllegalArgumentException, ClassCastException {
        return (E) componentMap.get(type);
    }

    @Override
    public <E extends Component> E removeComponent(System.Type type) throws IllegalArgumentException, ClassCastException {
        return (E) engine.removeComponentFromSystem(type, componentMap.remove(type));
    }

    public <E extends Component> E initializeComponent(System.Type type) {
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
        Set<Component> components = new HashSet<>(componentMap.values());
        clone.activity = activity;
        return null;
    }

    @Override
    public Entity getReplica() {
        return null;
    }

    public void reset() {
        detachDaughters();
        componentMap.clear();
    }

    public Engine engine() {
        return engine;
    }
}
