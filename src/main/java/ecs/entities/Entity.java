package ecs.entities;

import ecs.Engine;
import ecs.Scene;
import ecs.components.Component;
import ecs.components.Transform;
import ecs.util.ITurntable;
import ecs.util.Layer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Entity is an object that contains a bunch of components
 * that describes its nature
 *
 * @author cucumberbatch
 */
public class Entity implements IComponentManager, ITurntable {
    public Engine engine;
    public Layer layer;
    public String tag;

    // Transform component of that entity
    public Transform transform;

    // Transform component of the parent entity
    public Transform parent;

    // Collection of daughters entities
    public List<Transform> daughters;

    // Collection of components that attached to an entity
    public List<Component> componentList = new ArrayList<>();

    //
    public Map<String, Component> componentMap = new HashMap<>();

    // Activity state of the entity
    public boolean activity;


    public Entity(Engine engine, Scene scene) {
        this(engine, scene, Layer.DEFAULT, "Default");
    }

    public Entity(
            Engine engine,
            Scene scene,
            Layer layer,
            String tag) {
        this.engine = engine;
        this.engine.scene = scene;
        this.layer = layer;
        this.tag = tag;

        AddComponent(Transform::new);
        transform = GetComponent(Transform::new);

        /*
         Such a weird stuff right here...
         actually, its a necessary reference manipulation which will help you to get
         the access to a transform component from any component or even entity itself
        */
        transform.transform = transform;
    }

    /* Get the root entity in this branch */
    public Entity root() {
        Entity root = this;

        while (root.parent != null) {
            root = root.parent.entity;
        }

        return root;
    }

    public int compareTag(String tag) {
        return this.tag.compareTo(tag);
    }

    // ---------------------  Component access interface  ------------------------------------------------------

    @Override
    public <E extends Component> void AddComponent(Supplier<E> supplier) throws IllegalArgumentException, ClassCastException {
        E component = supplier.get();
        componentMap.put(component.getClass().getSimpleName(), component);
        engine.addComponentToSystem(component);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Component> E GetComponent(Supplier<E> supplier) throws IllegalArgumentException, ClassCastException {
        Class<?> clazz = supplier.get().getClass();
        Component component = componentMap.get(clazz.getSimpleName());
        // FIXME: duplicated expressions!!!
        if (component != null && !clazz.isInstance(component)) {
            throw new ClassCastException();
        }
        return (E) component;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Component> E RemoveComponent(Supplier<E> supplier) throws IllegalArgumentException, ClassCastException {
        Class<?> clazz = supplier.get().getClass();
        Component component = componentMap.remove(clazz.getSimpleName());
        engine.removeComponentFromSystem(component);
        // FIXME: duplicated expressions!!!
        if (component != null && !clazz.isInstance(component)) {
            throw new ClassCastException();
        }
        return (E) component;
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
}
