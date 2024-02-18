package org.north.core.managment;

import org.joml.Vector3f;
import org.north.core.components.Camera;
import org.north.core.components.Component;
import org.north.core.components.ComponentState;
import org.north.core.config.EngineConfig;
import org.north.core.context.ApplicationContext;
import org.north.core.exception.ComponentNotFoundException;
import org.north.core.reflection.di.Inject;
import org.north.core.reflection.di.registerer.DependencyRegisterer;
import org.north.core.reflection.initializer.ClassInitializer;
import org.north.core.reflection.scanner.ComponentHandlerScanner;
import org.north.core.systems.System;
import org.north.core.systems.TransformSystem;
import org.north.core.systems.command.AddComponentDeferredCommand;
import org.north.core.systems.command.DeferredCommand;
import org.north.core.systems.command.RemoveComponentDeferredCommand;
import org.north.core.systems.processes.CollisionHandlingProcess;
import org.north.core.systems.processes.InitProcess;
import org.north.core.systems.processes.RenderProcess;
import org.north.core.systems.processes.UpdateProcess;
import org.north.core.physics.collision.Collision;

import java.util.*;
import java.util.stream.Collectors;

public class SystemManager {
    private final Map<Class<? extends Component>, System<? extends Component>> systemMap;
    private final List<System<? extends Component>> systemList;

    private final Map<Class<? extends Component>, Class<? extends System<?>>> componentToSystemAssociations;
    private final List<DeferredCommand> deferredCommands;
    private final ComponentHandlerScanner scanner;
    private final DependencyRegisterer dependencyRegisterer;

    private Camera camera;

    public final List<InitProcess> listOfSystemsForInit              = new LinkedList<>();
    public final List<UpdateProcess> listOfSystemsForUpdate            = new LinkedList<>();
    public final List<RenderProcess> listOfSystemsForRender            = new LinkedList<>();
    public final List<System<? extends Component>> listOfSystemsForCollision         = new LinkedList<>();
    public final List<CollisionHandlingProcess> listOfSystemsForCollisionHandling = new LinkedList<>();

    public final List<Collision> collisions = new ArrayList<>();


    @Inject
    public SystemManager(ApplicationContext context) {
        dependencyRegisterer = context.getDependencyRegisterer();
        scanner = new ComponentHandlerScanner();
        deferredCommands = new LinkedList<>();
        componentToSystemAssociations = new HashMap<>();
        systemList = new ArrayList<>();
        systemMap = new HashMap<>();

        loadComponentSystemsFromPackage("org/north/core/systems");
    }

    private void loadComponentSystemsFromPackage(String packagePath) {
        try {
            // Logger.info(String.format("Searching for systems from package '%s'...", packagePath));
            List<ComponentHandlerScanner.Pair<?, ?>> annotatedClassesInPackage = scanner.getAnnotatedClassesInPackage(packagePath);
            annotatedClassesInPackage.forEach(pair -> componentToSystemAssociations.put(pair.component, pair.system));

            List<String> classNames = annotatedClassesInPackage.stream()
                    .map(pair -> pair.system.getName())
                    .collect(Collectors.toList());

            // Logger.info(String.format("Found %s system(s) classes: %s", classNames.size(), classNames));
        } catch (Exception e) {
            // Logger.error("Error while loading systems. Reason: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void groupSystemsByProcesses() {
        this.systemMap.values().forEach(this::attachToSystemLists);
    }

    private void attachToSystemLists(System<? extends Component> system) {
        Class<?> clazz = system.getClass();
        if (InitProcess.class.isAssignableFrom(clazz))               this.listOfSystemsForInit.add((InitProcess) system);
        if (UpdateProcess.class.isAssignableFrom(clazz))             this.listOfSystemsForUpdate.add((UpdateProcess) system);
        if (RenderProcess.class.isAssignableFrom(clazz))             this.listOfSystemsForRender.add((RenderProcess) system);
//        if (CollisionProcess.class.isAssignableFrom(clazz))          this.listOfSystemsForCollision.add(system);
        if (CollisionHandlingProcess.class.isAssignableFrom(clazz))  this.listOfSystemsForCollisionHandling.add((CollisionHandlingProcess) system);
    }

    public void setCameraComponent(Camera camera) {
        this.camera = camera;
    }

    public System<? extends Component> getSystem(Class<? extends Component> componentClass) {
        return systemMap.get(componentClass);
    }

    public void addComponent(Component component) {
        Class<? extends Component> componentClass = component.getClass();

        // initialize system if it is not
        if (systemMap.get(componentClass) == null) {
            try {
//                System<?> system = initializer.initSystem(componentToSystemAssociations.get(componentClass));
                Class<? extends System<?>> systemClass = componentToSystemAssociations.get(componentClass);
                System<?> system = dependencyRegisterer.registerDependency(systemClass);
                systemMap.put(componentClass, system);
                systemList.add(system);
                attachToSystemLists(system);
                // Logger.info(String.format("System %s initialized", system.getClass().getName()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // change state if it has no init process
        Class<?> system = systemMap.get(component.getClass()).getClass();
        if (!InitProcess.class.isAssignableFrom(system)) {
            component.setState(ComponentState.READY_TO_OPERATE_STATE);
        }

        systemMap.get(componentClass).addComponent(component);
    }

    @SuppressWarnings("unchecked")
    public <E extends Component> E getComponent(UUID componentId) {
        int systemCount = systemList.size();
        for (int i = 0; i < systemCount; i++) {
//        for (System<? extends Component> system: systemMap.values()) {
            System<? extends Component> system = systemList.get(i);
            Iterator<? extends Component> iterator = system.getComponentIterator();
            while (iterator.hasNext()) {
                Component component = iterator.next();
                if (component.getId().equals(componentId)) {
                    return (E) component;
                }
            }
        }
        throw new ComponentNotFoundException(componentId);
    }

    public <E extends Component> E removeComponent(Class<E> componentClass) {
        return null;//systemMap.get(componentClass).removeComponent(componentClass);
    }

    public void sortComponentsByDistanceToCamera(List<? extends Component> components) {
        if (camera == null) return;
        if (!cameraDistanceComparator.isCameraSet()) cameraDistanceComparator.setCamera(camera);

        components.sort(cameraDistanceComparator);
    }

    private final EntityDistanceToCameraComparator cameraDistanceComparator = new EntityDistanceToCameraComparator();

    static class EntityDistanceToCameraComparator implements Comparator<Component> {

        private Camera camera;
        private final Vector3f temp1 = new Vector3f();
        private final Vector3f temp2 = new Vector3f();

        public void setCamera(Camera camera) {
            this.camera = camera;
        }

        public boolean isCameraSet() {
            return this.camera != null;
        }

        @Override
        public int compare(Component o1, Component o2) {
            Vector3f cameraPosition = TransformSystem.getWorldPosition(camera.getTransform(), temp1);
            float o1Distance = TransformSystem.getWorldPosition(o1.getTransform(), temp2).distance(cameraPosition);
            float o2Distance = TransformSystem.getWorldPosition(o2.getTransform(), temp2).distance(cameraPosition);
            return (int) ((o2Distance - o1Distance) * 100f);
        }
    }

    public void addDeferredCommand(DeferredCommand command) {
        deferredCommands.add(command);
    }

    public void applyDeferredCommands() {
        for (DeferredCommand command: deferredCommands) {
            switch (command.getType()) {
                case ADD_COMPONENT: {
                    AddComponentDeferredCommand addComponentCommand = (AddComponentDeferredCommand) command;
                    addComponent(addComponentCommand.component);
                    break;
                }
                case REMOVE_COMPONENT: {
                    RemoveComponentDeferredCommand removeComponentCommand = (RemoveComponentDeferredCommand) command;
                    Component component = removeComponentCommand.component;
                    systemMap.get(component.getClass()).removeComponent(component.getId());
                    break;
                }
                default: break;
            }
        }
        deferredCommands.clear();
    }

}
