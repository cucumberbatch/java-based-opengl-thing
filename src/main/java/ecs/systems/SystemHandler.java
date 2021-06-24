package ecs.systems;

import ecs.components.ECSComponent;
import ecs.graphics.gl.Window;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static ecs.systems.AbstractECSSystem.*;

@SuppressWarnings("rawtypes")
public class SystemHandler {

    private final Map<ECSSystem.Type, ECSSystem> systemMap    = new HashMap<>();

    private final List<ECSSystem> systemList                  = new LinkedList<>();

    private final List<ECSSystem> listOfSystemsForInit        = new LinkedList<>();
    private final List<ECSSystem> listOfSystemsForUpdate      = new LinkedList<>();
    private final List<ECSSystem> listOfSystemsForRender      = new LinkedList<>();
    private final List<ECSSystem> listOfSystemsForCollision   = new LinkedList<>();
    private final List<ECSSystem> listOfSystemsForDestruction = new LinkedList<>();


    public boolean hasSystem(ECSSystem.Type type) {
        return systemMap.containsKey(type);
    }

    public void addSystem(ECSSystem.Type type, ECSSystem system) {
        if (systemMap.put(type, system) == null) attachToSystemLists(system);
    }

    // Attach system to
    private void attachToSystemLists(ECSSystem system) {
        int mask = system.getWorkflowMask();
        if ((mask & INIT_MASK) > 0)         listOfSystemsForInit.add(system);
        if ((mask & DESTRUCTION_MASK) > 0)  listOfSystemsForDestruction.add(system);

        if ((mask & COLLISION_MASK) > 0)    listOfSystemsForCollision.add(system);
        if ((mask & UPDATE_MASK) > 0)       listOfSystemsForUpdate.add(system);
        if ((mask & RENDER_MASK) > 0)       listOfSystemsForRender.add(system);
    }

    private void attachToInitList(ECSSystem system) {
        listOfSystemsForCollision.add(system);
    }

    private void detachFromInitList(ECSSystem system) {
        listOfSystemsForCollision.remove(system);
    }

    private void attachToDestructionList(ECSSystem system) {
        listOfSystemsForDestruction.add(system);
    }

    private void detachFromDestructionList(ECSSystem system) {
        listOfSystemsForDestruction.remove(system);
    }

    public ECSSystem getSystem(ECSSystem.Type type) {
        return systemMap.get(type);
    }


    public void linkComponentAndSystem(ECSSystem.Type type, ECSComponent component) {
        ECSSystem system = systemMap.get(type);
        system.addComponent(component);
        component.setSystem(system);
    }

    public void removeComponent(ECSSystem.Type type, ECSComponent component) {
        systemMap.get(type).removeComponent(component);
    }

    /* We are sure that each component object from system.getComponentList()
    is an ECSComponent type, because the only way of adding a component into
    component list is through component factory method call, that by definition
    generates objects of ECSComponent type */
    @SuppressWarnings("unchecked")
    public void init() {
        for (ECSSystem system : listOfSystemsForInit) {
            for (Object component : system.getComponentList()) {
                system.setCurrentComponent((ECSComponent) component);
                system.onInit();
            }
            detachFromInitList(system);
        }
    }

    /* We are sure that each component object from system.getComponentList()
    is an ECSComponent type, because the only way of adding a component into
    component list is through component factory method call, that by definition
    generates objects of ECSComponent type */
    @SuppressWarnings("unchecked")
    public void update(float deltaTime) {
        for (ECSSystem system : listOfSystemsForUpdate) {
            for (Object component : system.getComponentList()) {
                system.setCurrentComponent((ECSComponent) component);
                system.onUpdate(deltaTime);
            }
        }
    }

    /* We are sure that each component object from system.getComponentList()
    is an ECSComponent type, because the only way of adding a component into
    component list is through component factory method call, that by definition
    generates objects of ECSComponent type */
    @SuppressWarnings("unchecked")
    public void render(Window window) {
        for (ECSSystem system : listOfSystemsForRender) {
            for (Object component : system.getComponentList()) {
                system.setCurrentComponent((ECSComponent) component);
                system.onRender(window);
            }
        }
    }

    /* We are sure that each component object from system.getComponentList()
    is an ECSComponent type, because the only way of adding a component into
    component list is through component factory method call, that by definition
    generates objects of ECSComponent type */
    @SuppressWarnings("unchecked")
    public void destroy() {
        for (ECSSystem system : listOfSystemsForRender) {
            for (Object component : system.getComponentList()) {
                system.setCurrentComponent((ECSComponent) component);
                system.onDestroy();
            }
            detachFromDestructionList(system);
        }
    }

    /* We are sure that each component object from system.getComponentList()
    is an ECSComponent type, because the only way of adding a component into
    component list is through component factory method call, that by definition
    generates objects of ECSComponent type */
    @SuppressWarnings("unchecked")
    public void collide() {
        for (ECSSystem system : listOfSystemsForCollision) {
            for (Object component : system.getComponentList()) {
                system.setCurrentComponent((ECSComponent) component);
//                system.collide(collision);
            }
        }
    }
}
