package ecs.architecture.entity;

import ecs.components.Component;

import java.util.LinkedList;
import java.util.List;

public class Entity {
    public long id;
    public String name;
    public Entity parent;
    public List<Entity> daughters = new LinkedList<>();
    public List<Component> components = new LinkedList<>();
}
