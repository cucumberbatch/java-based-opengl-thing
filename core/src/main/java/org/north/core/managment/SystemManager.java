package org.north.core.managment;

import org.joml.Vector3f;
import org.north.core.component.Camera;
import org.north.core.component.Component;
import org.north.core.component.ComponentState;
import org.north.core.context.ApplicationContext;
import org.north.core.exception.ComponentNotFoundException;
import org.north.core.reflection.di.Inject;
import org.north.core.reflection.di.registerer.DependencyRegisterer;
import org.north.core.reflection.scanner.ComponentHandlerScanner;
import org.north.core.system.System;
import org.north.core.system.command.AddComponentDeferredCommand;
import org.north.core.system.command.DeferredCommand;
import org.north.core.system.command.RemoveComponentDeferredCommand;
import org.north.core.system.process.CollisionHandlingProcess;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.RenderProcess;
import org.north.core.system.process.UpdateProcess;
import org.north.core.physics.collision.Collision;

import java.util.*;
import java.util.stream.Collectors;

public class SystemManager {
    public final List<InitProcess<? extends Component>> listOfSystemsForInit;
    public final List<UpdateProcess<? extends Component>> listOfSystemsForUpdate;
    public final List<RenderProcess<? extends Component>> listOfSystemsForRender;
    public final List<System<? extends Component>> listOfSystemsForCollision;
    public final List<CollisionHandlingProcess<? extends Component>> listOfSystemsForCollisionHandling;

    public final List<Collision> collisions;

    private final Map<Class<? extends Component>, System<? extends Component>> systemMap;
    private final List<System<? extends Component>> systemList;
    private final Map<Class<? extends Component>, Class<? extends System<?>>> componentToSystemAssociations;
    private final List<DeferredCommand> deferredCommands;
    private final ComponentHandlerScanner scanner;
    private final DependencyRegisterer dependencyRegisterer;
    private final EntityDistanceToCameraComparator cameraDistanceComparator;

    private Camera camera;

    @Inject
    public SystemManager(ApplicationContext context) {
        dependencyRegisterer = context.getDependencyRegisterer();
        scanner = new ComponentHandlerScanner();
        deferredCommands = new LinkedList<>();
        componentToSystemAssociations = new HashMap<>();
        systemList = new ArrayList<>();
        systemMap = new HashMap<>();

        loadComponentSystemsFromPackage("org/north/core/system");
        cameraDistanceComparator = new EntityDistanceToCameraComparator();
        collisions = new ArrayList<>();
        listOfSystemsForCollisionHandling = new LinkedList<>();
        listOfSystemsForCollision = new LinkedList<>();
        listOfSystemsForRender = new LinkedList<>();
        listOfSystemsForUpdate = new LinkedList<>();
        listOfSystemsForInit = new LinkedList<>();
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

    private void attachToSystemLists(System<? extends Component> system) {
        Class<?> clazz = system.getClass();
        if (InitProcess.class.isAssignableFrom(clazz)) {
            this.listOfSystemsForInit.add((InitProcess<? extends Component>) system);
        }
        if (UpdateProcess.class.isAssignableFrom(clazz)) {
            this.listOfSystemsForUpdate.add((UpdateProcess<? extends Component>) system);
        }
        if (RenderProcess.class.isAssignableFrom(clazz)) {
            this.listOfSystemsForRender.add((RenderProcess<? extends Component>) system);
        }
//        if (CollisionProcess.class.isAssignableFrom(clazz)) {
//            this.listOfSystemsForCollision.add(system);
//        }
        if (CollisionHandlingProcess.class.isAssignableFrom(clazz)) {
            this.listOfSystemsForCollisionHandling.add((CollisionHandlingProcess<? extends Component>) system);
        }
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
                if (systemClass == null) return;
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

    public void addDeferredCommand(DeferredCommand command) {
        deferredCommands.add(command);
    }

    public void applyDeferredCommands() {
        for (DeferredCommand command : deferredCommands) {
            switch (command.getType()) {
                case ADD_COMPONENT: {
                    AddComponentDeferredCommand addComponentCommand = (AddComponentDeferredCommand) command;
                    addComponent(addComponentCommand.component);
                    break;
                }
                case REMOVE_COMPONENT: {
                    RemoveComponentDeferredCommand removeComponentCommand = (RemoveComponentDeferredCommand) command;
                    Component component = removeComponentCommand.component;
                    component.setActivity(false);
                    systemMap.get(component.getClass()).removeComponent(component.getId());
                    break;
                }
                default:
                    break;
            }
        }
        deferredCommands.clear();
    }

    public void reset() {
        for (System<?> system: systemList) {
            system.reset();
        }
    }

    static class EntityDistanceToCameraComparator implements Comparator<Component> {

        private final Vector3f temp1 = new Vector3f();
        private final Vector3f temp2 = new Vector3f();
        private Camera camera;

        public void setCamera(Camera camera) {
            this.camera = camera;
        }

        public boolean isCameraSet() {
            return this.camera != null;
        }

        @Override
        public int compare(Component o1, Component o2) {
            Vector3f cameraPosition = camera.getTransform().getGlobalPosition(temp1);
            float o1Distance = o1.getTransform().getGlobalPosition(temp2).distance(cameraPosition);
            float o2Distance = o2.getTransform().getGlobalPosition(temp2).distance(cameraPosition);
            return (int) ((o2Distance - o1Distance) * 100f);
        }
    }

}
