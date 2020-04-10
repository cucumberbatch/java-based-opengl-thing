package ecs;

import ecs.components.Component;
import ecs.components.Rigidbody;
import ecs.components.Transform;
import ecs.systems.RigidbodySystem;
import ecs.systems.System;
import ecs.systems.TransformSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SystemHelper {
    public Map<String, Supplier> componentSystemMap = new HashMap<>();

    public SystemHelper() {
        componentSystemMap.put(Transform.class.getSimpleName(), TransformSystem::new);
        componentSystemMap.put(Rigidbody.class.getSimpleName(), RigidbodySystem::new);
    }

    @SuppressWarnings("unchecked")
    public <Type extends System> Type getSystemByComponentName(String name) {
        return (Type) (componentSystemMap.get(name)).get();
    }

    @SuppressWarnings("unchecked")
    public <sType extends System, cType extends Component> void linkComponentAndSystem(cType component) {
        component.system = (sType) componentSystemMap.get(component.getClass().getSimpleName()).get();
    }

}
