package ecs.entities;

import ecs.Engine;
import ecs.components.Component;
import ecs.components.Transform;
import ecs.util.ITurntable;
import ecs.util.Layer;

import java.util.*;
import java.util.function.Supplier;

public class Entity implements IComponentManager, ITurntable {
    public Engine engine;
    public Layer layer;
    public String tag;

    public Transform parent;
    public List<Transform> daughters;

    public Transform transform;
    public List<Component> componentList = new ArrayList<>();
    public Map<String, Component> componentMap = new HashMap<>();

    public boolean activity;

    public Entity() {

    }

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



    @Override
    public <E extends Component> void AddComponent(Supplier<E> supplier) throws IllegalArgumentException, ClassCastException {
        E component = supplier.get();
        componentMap.put(component.getClass().getSimpleName(), component);
    }

    @Override
    public <E extends Component> E GetComponent(Supplier<E> supplier) throws IllegalArgumentException, ClassCastException {
        Component component = componentMap.get(supplier.get().getClass().getSimpleName());
        return (E) component;
    }

    @Override
    public <E extends Component> E RemoveComponent(Supplier<E> supplier) throws IllegalArgumentException, ClassCastException {
//        E component = clazz.cast(componentMap.remove(clazz.getSimpleName()));
//        return engine.removeComponentFromSystem(component);
        return null;
    }

    @Override
    public boolean isActive() { return activity; }

    @Override
    public void setActivity(boolean activity) { this.activity = activity; }

    @Override
    public void switchActivity() { activity = !activity; }
}
