package ecs.managment;

import ecs.components.*;
import ecs.exception.ComponentNotFoundException;
import ecs.reflection.scanner.ComponentHandlerScanner;
import ecs.systems.*;
import ecs.systems.System;
import ecs.systems.processes.CollisionHandlingProcess;
import ecs.systems.processes.InitProcess;
import ecs.systems.processes.RenderProcess;
import ecs.systems.processes.UpdateProcess;
import ecs.utils.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class SystemManager {
    private static SystemManager instance;

    public final Map<Class<? extends Component>, System<? extends Component>> systemMap  = new HashMap<>();

    public final List<InitProcess> listOfSystemsForInit              = new LinkedList<>();
    public final List<UpdateProcess> listOfSystemsForUpdate            = new LinkedList<>();
    public final List<RenderProcess> listOfSystemsForRender            = new LinkedList<>();
    public final List<System<? extends Component>> listOfSystemsForCollision         = new LinkedList<>();
    public final List<CollisionHandlingProcess> listOfSystemsForCollisionHandling = new LinkedList<>();

    public final List<Collision> collisions = new ArrayList<>();

    private SystemManager() {
        loadComponentSystemsFromPackage("ecs/systems");
        //todo: implement lazy system initialization
        groupSystemsByProcesses();
    }

    public static SystemManager getInstance() {
        if (instance == null) {
            instance = new SystemManager();
        }
        return instance;
    }

    private void loadComponentSystemsFromPackage(String packagePath) {
        try {
            Logger.info(String.format("Searching for systems from package '%s'...", packagePath));
            ComponentHandlerScanner scanner = new ComponentHandlerScanner();
            List<ComponentHandlerScanner.Pair<?, ?>> annotatedClassesInPackage = scanner.getAnnotatedClassesInPackage(packagePath);
            annotatedClassesInPackage.forEach(pair -> systemMap.put(pair.component, pair.system));

            List<String> classNames = annotatedClassesInPackage.stream()
                    .map(pair -> pair.system.getClass().getName())
                    .collect(Collectors.toList());

            Logger.info(String.format("Found and loaded %s system(s) classes: %s", classNames.size(), classNames));
        } catch (Exception e) {
            Logger.error("Error while loading systems. Reason: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void groupSystemsByProcesses() {
        this.systemMap.values().forEach(this::attachToSystemLists);
    }

    private void attachToSystemLists(System<? extends Component> system) {
        Class<? extends System> clazz = system.getClass();
        if (InitProcess.class.isAssignableFrom(clazz))               this.listOfSystemsForInit.add((InitProcess) system);
        if (UpdateProcess.class.isAssignableFrom(clazz))             this.listOfSystemsForUpdate.add((UpdateProcess) system);
        if (RenderProcess.class.isAssignableFrom(clazz))             this.listOfSystemsForRender.add((RenderProcess) system);
//        if (CollisionProcess.class.isAssignableFrom(clazz))          this.listOfSystemsForCollision.add(system);
        if (CollisionHandlingProcess.class.isAssignableFrom(clazz))  this.listOfSystemsForCollisionHandling.add((CollisionHandlingProcess) system);
    }

    public void addComponent(Component component) {
        changeStateIfHasNoInitProcess(component);
        Class<? extends Component> componentClass = component.getClass();
        this.systemMap.get(componentClass).addComponent(component);
    }

    private void changeStateIfHasNoInitProcess(Component component) {
        Class<?> system = systemMap.get(component.getClass()).getClass();
        if (!InitProcess.class.isAssignableFrom(system)) {
            component.setState(AbstractComponent.READY_TO_OPERATE_STATE);
        }
    }

    @SuppressWarnings("unchecked")
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
