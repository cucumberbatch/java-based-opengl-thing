package ecs.managment;

import ecs.components.*;
import ecs.exception.ComponentNotFoundException;
import ecs.reflection.scanner.ComponentHandlerScanner;
import ecs.systems.*;
import ecs.systems.System;
import ecs.utils.Logger;

import java.util.*;

import static ecs.systems.AbstractSystem.*;

public class SystemManager {
    private static SystemManager instance;

    public final Map<Class<? extends Component>, System<? extends Component>> systemMap  = new HashMap<>();

    public final List<System<? extends Component>> listOfSystemsForInit              = new LinkedList<>();
    public final List<System<? extends Component>> listOfSystemsForUpdate            = new LinkedList<>();
    public final List<System<? extends Component>> listOfSystemsForRender            = new LinkedList<>();
    public final List<System<? extends Component>> listOfSystemsForCollision         = new LinkedList<>();
    public final List<System<? extends Component>> listOfSystemsForCollisionHandling = new LinkedList<>();

    public final List<Collision> collisions = new ArrayList<>();

    private SystemManager() {
        initComponentSystemAssociation();
        groupSystemsByProcesses();
    }

    public static SystemManager getInstance() {
        if (instance == null) {
            instance = new SystemManager();
        }
        return instance;
    }

    private void initComponentSystemAssociation() {
        try {
            ComponentHandlerScanner scanner = new ComponentHandlerScanner();
            scanner.getAnnotatedClassesInPackage("ecs/systems")
                    .forEach(pair -> systemMap.put(pair.component, pair.system));
        } catch (Exception e) {
            Logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void groupSystemsByProcesses() {
        this.systemMap.values().forEach(this::attachToSystemLists);
    }

    private void attachToSystemLists(System<? extends Component> system) {
        int mask = system.getWorkflowMask();
        if ((mask & INIT_MASK) > 0)               this.listOfSystemsForInit.add(system);
        if ((mask & UPDATE_MASK) > 0)             this.listOfSystemsForUpdate.add(system);
        if ((mask & RENDER_MASK) > 0)             this.listOfSystemsForRender.add(system);
        if ((mask & COLLISION_MASK) > 0)          this.listOfSystemsForCollision.add(system);
        if ((mask & COLLISION_HANDLING_MASK) > 0) this.listOfSystemsForCollisionHandling.add(system);
    }

    public void addComponent(Component component) {
        Class<? extends Component> componentClass = component.getClass();
        this.systemMap.get(componentClass).addComponent(component);
    }

    public <E extends Component> E getComponent(long componentId) {
        for (System<? extends Component> system: systemMap.values()) {
            for (Component component: system.getComponentList()) {
                if (component.getId() == componentId) {
                    return (E) component;
                }
            }
        }
        throw new ComponentNotFoundException(componentId);
    }

}
