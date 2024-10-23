package org.north.core.management;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import org.north.core.component.Camera;
import org.north.core.component.Component;
import org.north.core.component.ComponentState;
import org.north.core.config.ApplicationProperties;
import org.north.core.context.ApplicationContext;
import org.north.core.exception.ComponentNotFoundException;
import org.north.core.graphics.Window;
import org.north.core.physics.collision.Collision;
import org.north.core.reflection.di.Inject;
import org.north.core.reflection.scanner.ComponentHandlerScanner;
import org.north.core.system.System;
import org.north.core.system.command.DeferredCommand;
import org.north.core.system.process.InitProcess;
import org.north.core.system.process.Process;

import java.util.*;
import java.util.stream.Collectors;

public class SystemManager implements Resettable {
    private static final Logger log = LogManager.getLogger();

    public final List<Collision> collisions;
    private final Map<Class<? extends Process>, List<Process>> processMap;
    private final Map<Class<? extends Component>, System<?>> systemMap;
    private final List<System<?>> systemList;
    private final Map<Class<? extends Component>, Class<? extends System<?>>> componentToSystemAssociations;
    private final Queue<DeferredCommand> deferredCommands;
    private final ComponentHandlerScanner scanner;
    private final ApplicationContext applicationContext;
    private final EntityDistanceToCameraComparator cameraDistanceComparator;

    private Camera camera;

    @Inject
    public SystemManager(ApplicationContext context) {
        applicationContext = context;
        scanner = new ComponentHandlerScanner();
        cameraDistanceComparator = new EntityDistanceToCameraComparator();
        deferredCommands = new LinkedList<>();
        componentToSystemAssociations = new HashMap<>();
        collisions = new ArrayList<>();
        systemList = new ArrayList<>();
        systemMap = new HashMap<>();
        processMap = new HashMap<>();

        initProcessMap(ApplicationProperties.getProperty("process.package"));
        loadComponentSystemsFromPackage(ApplicationProperties.getProperty("system.package"));
    }

    public <ProcessType extends Process> List<ProcessType> getProcessList(Class<ProcessType> processTypeClass) {
        List<ProcessType> processList = new ArrayList<>();
        for (Process process : processMap.get(processTypeClass)) {
            processList.add(processTypeClass.cast(process));
        }
        return processList;
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

    private void initProcessMap(String processPackageName) {
        try {
            List<Class<? extends Process>> processClasses = scanner.getAllProcessClasses(processPackageName);
            for (Class<? extends Process> processClass : processClasses) {
                processMap.put(processClass, new ArrayList<>());
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void attachToSystemLists(System<?> system) {
        Class<?> clazz = system.getClass();
        for (Class<? extends Process> processClass : processMap.keySet()) {
            if (processClass.isAssignableFrom(clazz)) {
                processMap.get(processClass).add((Process) system);
            }
        }
    }

    public void setCameraComponent(Camera camera) {
        this.camera = camera;
    }

    public System<?> getSystem(Class<? extends Component> componentClass) {
        return systemMap.get(componentClass);
    }

    @SuppressWarnings("unchecked")
    public <ComponentInstance extends Component> ComponentInstance addComponent(ComponentInstance component) {
        Class<? extends Component> componentClass = component.getClass();

        // initialize system if it is not
        if (systemMap.get(componentClass) == null) {
            try {
                //                System<?> system = initializer.initSystem(componentToSystemAssociations.get(componentClass));
                Class<? extends System<?>> systemClass = componentToSystemAssociations.get(componentClass);
                if (systemClass == null) return null;
                System<?> system = applicationContext.addDependency(systemClass);
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

        return (ComponentInstance) systemMap.get(componentClass).addComponent(component);
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
            command.execute(this);
            log.info("executed deferred command: {}", command);
        }
        deferredCommands.clear();
    }

    @Override
    public void reset() {
        for (System<?> system : systemList) {
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
