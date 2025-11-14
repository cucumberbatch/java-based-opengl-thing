package org.north.core.management;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.joml.Vector3f;
import org.north.core.component.Camera;
import org.north.core.component.Component;
import org.north.core.component.ComponentState;
import org.north.core.component.Transform;
import org.north.core.config.ApplicationProperties;
import org.north.core.context.ApplicationContext;
import org.north.core.exception.ComponentNotFoundException;
import org.north.core.management.data.OctreeNode;
import org.north.core.physics.collision.Collision;
import org.north.core.reflection.scanner.ComponentHandlerScanner;
import org.north.core.system.System;
import org.north.core.system.command.DeferredCommand;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.Process;
import org.north.core.system.process.RenderProcess;

import java.util.*;
import java.util.stream.Collectors;

public class SystemManager implements Resettable {
    private static final Logger log = LoggerFactory.getLogger(SystemManager.class);

    /**
     * Collisions that was registered in an octree during a frame computation
     */
    public final List<Collision> collisions;

    public final Map<Class<? extends Process>, List<Process>> processMap;
    public final Map<Class<? extends Component>, System<?>> systemMap;
    public final List<System<?>> systemList;
    public final Map<Class<? extends Component>, Class<? extends System<?>>> componentToSystemAssociations;
    public final Queue<DeferredCommand> deferredCommands;
    public final ComponentHandlerScanner scanner;
    public final ApplicationContext applicationContext;
    public final EntityDistanceToCameraComparator cameraDistanceComparator;
    public final OctreeNode<Transform> spatialTree;

    private Camera camera;

    public SystemManager(ApplicationContext context) {
        applicationContext = context;
        scanner = new ComponentHandlerScanner();
        cameraDistanceComparator = new EntityDistanceToCameraComparator();
        deferredCommands = new LinkedList<>();
        componentToSystemAssociations = new IdentityHashMap<>();
        spatialTree = new OctreeNode<>(new Vector3f(0, 0, 0), new Vector3f(8, 8, 8));
        collisions = new ArrayList<>();
        systemList = new ArrayList<>();
        systemMap = new IdentityHashMap<>();
        processMap = new IdentityHashMap<>();

        initProcessMap(ApplicationProperties.getProperty("process.package"));
        loadComponentSystemsFromPackage(ApplicationProperties.getProperty("system.package"));
    }

    @SuppressWarnings("unchecked")
    public <P extends Process> List<P> getProcessList(Class<P> processType) {
        return (List<P>) processMap.get(processType);
    }

    private void loadComponentSystemsFromPackage(String packagePath) {
        try {
            // Logger.info(String.format("Searching for systems from package '%s'...", packagePath));
            List<ComponentHandlerScanner.SystemComponentPair<?, ?>> annotatedClassesInPackage = scanner.getAnnotatedClassesInPackage(packagePath);
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

    private void initProcessMap(String processPackageName) {
        try {
            log.info("processPackageName: " + processPackageName);
            List<Class<? extends Process>> processClasses = scanner.getAllProcessClasses(processPackageName);
            log.info("found process classes: " + processClasses);
            for (Class<? extends Process> processClass : processClasses) {
                processMap.put(processClass, new ArrayList<>());
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void setCameraComponent(Camera camera) {
        this.camera = camera;
    }

    public System<?> getSystemByComponentType(Class<? extends Component> componentType) {
        return systemMap.get(componentType);
    }

    @SuppressWarnings("unchecked")
    public <C extends Component> C addComponent(C component) {
        Class<? extends Component> componentClass = component.getClass();
        System<?> system;

        // initialize system if it is not
        if ((system = systemMap.get(componentClass)) == null) {
            try {
                //                System<?> system = initializer.initSystem(componentToSystemAssociations.get(componentClass));
                Class<? extends System<?>> systemClass = componentToSystemAssociations.get(componentClass);
                if (systemClass == null) return null;
                system = applicationContext.addDependency(systemClass);
                systemMap.put(componentClass, system);
                systemList.add(system);

                // Attaching to system list
                for (Class<? extends Process> processClass : processMap.keySet()) {
                    if (processClass.isAssignableFrom(systemClass)) {
                        processMap.get(processClass).add((Process) system);
                    }
                }

                // Logger.info(String.format("System %s initialized", system.getClass().getName()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // change state if it has no init process
        if (!InitProcess.class.isAssignableFrom(system.getClass())) {
            component.setState(ComponentState.READY_TO_OPERATE_STATE);
        }

        if (component instanceof Transform) {
            spatialTree.insert((Transform) component);
        }

        return (C) system.addComponent(component);
    }

    @SuppressWarnings("unchecked")
    public <E extends Component> E getComponent(UUID componentId) {
        for (System<?> system : systemList) {
            Component component = system.getComponent(componentId);
            if (component != null) {
                return (E) component;
            }
        }
        throw new ComponentNotFoundException(componentId);
    }

    private Map<RenderProcess<?>, List<? extends Component>> scm = new IdentityHashMap<>();

    public List<? extends Component> sortComponentsByDistanceToCamera(RenderProcess<?> process) {
        if (camera == null) return Collections.emptyList();
        if (!cameraDistanceComparator.isCameraSet()) cameraDistanceComparator.setCameraPosition(camera.getTransform().getPosition());

        List<? extends Component> components;
        if (scm.containsKey(process)) {
            components = scm.get(process);
        } else {
            System<? extends Component> system = (System<? extends Component>) process;
            components = system.getComponentList();
            scm.put(process, components);
        }

        components.sort(cameraDistanceComparator);
        return components;
    }

    public void addDeferredCommand(DeferredCommand command) {
        deferredCommands.add(command);
    }

    public void applyDeferredCommands() {
        if (deferredCommands.isEmpty()) return;
        for (DeferredCommand command : deferredCommands) {
            command.execute(this);
//            log.info("executed deferred command: {}", command);
        }
        deferredCommands.clear();
    }

    @Override
    public void reset() {
        for (System<?> system : systemList) {
            system.reset();
        }
    }

    public void registerCollisions() {
        Collection<Collision> foundCollisions = spatialTree.getAllCollisions();
    }

    static class EntityDistanceToCameraComparator implements Comparator<Component> {
        private Vector3f cameraPosition;

        //note: we need to be careful because of passing a reference to camera position only once at the start of Camera component life,
        // so if we accidentally replace Transform.position vec instance we loose all position changes and scene will be rendered in wrong order
        public void setCameraPosition(Vector3f cameraPosition) {
            this.cameraPosition = cameraPosition;
        }

        public boolean isCameraSet() {
            return this.cameraPosition != null;
        }

        @Override
        public int compare(Component o1, Component o2) {
            float o1Distance = o1.getTransform().cachedGlobalPosition.distance(cameraPosition);
            float o2Distance = o2.getTransform().cachedGlobalPosition.distance(cameraPosition);
            return Float.compare(o2Distance, o1Distance);
        }
    }

}
