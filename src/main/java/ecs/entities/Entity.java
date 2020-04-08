package ecs.entities;

import ecs.Engine;
import ecs.components.Component;
import ecs.util.Layer;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    public Engine engine;
    public Layer layer;
    public String tag;
    public List<Component> componentList = new ArrayList<>();

}
