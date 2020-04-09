package ecs.entities;

import ecs.Engine;
import ecs.components.Component;
import ecs.components.Transform;
import ecs.util.ITurntable;
import ecs.util.Layer;

import java.util.*;

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
        Entity that = this;

        while (that.parent != null) {
            that = that.parent.entity;
        }

        return that;
    }

    public int compareTag(String tag) {
        return this.tag.compareTo(tag);
    }


    @Override
    public <E extends Component> void AddComponent(Class<E> clazz) throws IllegalArgumentException {
        E component = clazz.cast(new Component());

    }

    @Override
    public <E extends Component> void RemoveComponent(Class<E> clazz) throws IllegalArgumentException {

    }

    @Override
    public <E extends Component> E GetComponent(Class<E> clazz) throws IllegalArgumentException, ClassCastException {
        return clazz.cast(componentMap.get(clazz.getName()));
    }

    @Override
    public boolean isActive() { return activity; }

    @Override
    public void setActivity(boolean activity) { this.activity = activity; }

    @Override
    public void switchActivity() { activity = !activity; }
}
