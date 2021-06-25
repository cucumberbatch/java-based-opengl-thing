package ecs.systems;

import ecs.components.ECSComponent;
import ecs.graphics.gl.Window;

import java.util.*;

import static ecs.systems.AbstractECSSystem.*;

@SuppressWarnings("rawtypes")
public class SystemHandler {

    private final Map<ECSSystem.Type, ECSSystem> systemMap    = new HashMap<>();

    private final List<ECSSystem> systemList                  = new LinkedList<>();

    private final List<ECSSystem> listOfSystemsForInit        = new ArrayList<>();
    private final List<ECSSystem> listOfSystemsForUpdate      = new ArrayList<>();
    private final List<ECSSystem> listOfSystemsForRender      = new ArrayList<>();
    private final List<ECSSystem> listOfSystemsForCollision   = new ArrayList<>();
    private final List<ECSSystem> listOfSystemsForDestruction = new ArrayList<>();

    // needs for iterating over the systems
    private int index;

    private StringBuffer systemStatusAccumulator = new StringBuffer();

    public boolean hasSystem(ECSSystem.Type type) {
        return systemMap.containsKey(type);
    }

    public void addSystem(ECSSystem.Type type) {
        if (systemMap.containsKey(type)) {
            return;
        }

        ECSSystem system = type.createSystem();

        if (systemMap.put(type, system) == null) {
            attachToSystemLists(system);
        }
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
        listOfSystemsForInit.add(system);
    }

    private void detachFromInitList(ECSSystem system) {
        listOfSystemsForInit.remove(system);
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
    generates objects of ECSComponent type, also we don't use iterators because
    we don't want to create them each frame, so it uses a simple indexed for loop
    there and in other methods below */
    @SuppressWarnings("unchecked")
    public void init() {
        List<ECSSystem> systems = listOfSystemsForInit;

        for (index = systems.size() - 1; index >= 0; index--) {
            for (Object component : systems.get(index).getComponentList()) {
                systems.get(index).setComponent((ECSComponent) component);
                systems.get(index).onInit();
            }
            systemStatusAccumulator.append("INIT: ").append(systems.get(index)).append("\n");
            detachFromInitList(systems.get(index));
        }
    }

    @SuppressWarnings("unchecked")
    public void update(float deltaTime) {
        List<ECSSystem> systems = listOfSystemsForUpdate;

        for (index = systems.size() - 1; index >= 0; index--) {
            for (Object component : systems.get(index).getComponentList()) {
                systems.get(index).setComponent((ECSComponent) component);
                systems.get(index).onUpdate(deltaTime);
            }
            systemStatusAccumulator.append("UPDATE: ").append(systems.get(index)).append("\n");
        }
    }

    @SuppressWarnings("unchecked")
    public void render(Window window) {
        List<ECSSystem> systems = listOfSystemsForRender;

        for (index = systems.size() - 1; index >= 0; index--) {
            for (Object component : systems.get(index).getComponentList()) {
                systems.get(index).setComponent((ECSComponent) component);
                systems.get(index).onRender(window);
            }
            systemStatusAccumulator.append("RENDER: ").append(systems.get(index)).append("\n");
        }
    }

    @SuppressWarnings("unchecked")
    public void destroy() {
        List<ECSSystem> systems = listOfSystemsForDestruction;

        for (index = systems.size() - 1; index >= 0; index--) {
            for (Object component : systems.get(index).getComponentList()) {
                systems.get(index).setComponent((ECSComponent) component);
                systems.get(index).onDestroy();
            }
            systemStatusAccumulator.append("DESTRUCT: ").append(systems.get(index)).append("\n");
            detachFromDestructionList(systems.get(index));
        }
    }

    @SuppressWarnings("unchecked")
    public void collide() {
    }

    public String getSystemsStatus() {
        String data = systemStatusAccumulator.toString();
        systemStatusAccumulator = new StringBuffer();
        return data;
    }
}
