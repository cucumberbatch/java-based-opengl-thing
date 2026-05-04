package org.north.core.system;

import org.joml.Vector3f;
import org.north.core.component.Camera;
import org.north.core.component.Component;
import org.north.core.component.Transform;
import org.north.core.component.management.ComponentContainer;
import org.north.core.reflection.di.Inject;
import org.north.core.system.process.RenderProcess;

import java.util.*;

public class CameraAwareService {
    private final ComponentContainer componentContainer;
    private final CameraDistanceComparator cameraDistanceComparator;
    private final Map<RenderProcess<?>, List<? extends Component>> distanceSortedComponentsMap;
    private Camera camera;

    @Inject
    public CameraAwareService(ComponentContainer componentContainer) {
        this.componentContainer = componentContainer;
        cameraDistanceComparator = new CameraDistanceComparator(componentContainer);
        distanceSortedComponentsMap = new IdentityHashMap<>();
    }

    public void setCameraComponent(Camera camera) {
        this.camera = camera;
    }

    public List<? extends Component> sortComponentsByDistanceToCamera(RenderProcess<?> process) {
        if (camera == null) return Collections.emptyList();
        if (!cameraDistanceComparator.isCameraSet()) {
            Transform transform = componentContainer.get(camera.getEntity(), Transform.class);
            cameraDistanceComparator.setCameraPosition(transform.getPosition());
        }

        List<? extends Component> components;
        if (distanceSortedComponentsMap.containsKey(process)) {
            components = distanceSortedComponentsMap.get(process);
        } else {
            System<? extends Component> system = (System<? extends Component>) process;
            components = system.getComponentList();
            distanceSortedComponentsMap.put(process, components);
        }

        components.sort(cameraDistanceComparator);
        return components;
    }

    static class CameraDistanceComparator implements Comparator<Component> {
        private Vector3f cameraPosition;
        private final ComponentContainer componentContainer;

        public CameraDistanceComparator(ComponentContainer componentContainer) {
            this.componentContainer = componentContainer;
        }

        //note: we need to be careful because of passing a reference to camera position only once at the start of Camera component life,
        // so if we accidentally replace Transform.position vec instance we loose all position changes and scene will be rendered in wrong order
        public void setCameraPosition(Vector3f cameraPosition) {
            this.cameraPosition = cameraPosition;
        }

        public boolean isCameraSet() {
            return this.cameraPosition != null;
        }

        private final Vector3f o1Position = new Vector3f();
        private final Vector3f o2Position = new Vector3f();

        @Override
        public int compare(Component o1, Component o2) {
            Transform t1 = componentContainer.get(o1.getEntity(), Transform.class);
            Transform t2 = componentContainer.get(o2.getEntity(), Transform.class);
            float o1Distance = t1.getGlobalPosition(o1Position).distance(cameraPosition);
            float o2Distance = t2.getGlobalPosition(o2Position).distance(cameraPosition);
            return Float.compare(o2Distance, o1Distance);
        }
    }

}
