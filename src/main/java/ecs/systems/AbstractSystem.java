package ecs.systems;

import ecs.Engine;
import ecs.components.Component;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractSystem implements ISystem {
    public Engine engine;
    public List<Component> componentList = new LinkedList<>();

}
