package ecs.systems;

import ecs.Engine;
import ecs.components.Component;

import java.util.LinkedList;
import java.util.List;

public abstract class System implements IStart, IUpdate {
    public Engine engine;
    public List<Component> componentList = new LinkedList<>();


}
