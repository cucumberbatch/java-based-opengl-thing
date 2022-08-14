package ecs.systems;

import ecs.components.*;
import ecs.entities.Entity;
import ecs.entities.IComponentManager;
import ecs.systems.processes.ISystem;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public interface ECSSystem<E extends ECSComponent> extends ISystem, IComponentManager {

    /* Type of system */
    enum Type {
        RENDERER         (() -> new Renderer(),         () -> new RendererSystem()),
        TRANSFORM        (() -> new Transform(),        () -> new TransformSystem()),
        RIGIDBODY        (() -> new RigidBody(),        () -> new RigidBodySystem()),
        RIGIDBODY2D      (() -> new RigidBody2d(),      () -> new RigidBody2dSystem()),
        CAMERA           (() -> new Camera(),           () -> new CameraSystem()),
        PLANE            (() -> new PlaneRenderer(),    () -> new PlaneRendererSystem()),
        MESH_COLLIDER    (() -> new MeshCollider(),     () -> new MeshColliderSystem()),
        BUTTON           (() -> new Button(),           () -> new ButtonSystem());

        private final Supplier<ECSComponent> componentSupplier;
        private final Supplier<ECSSystem<? extends ECSComponent>> systemSupplier;

        Type(Supplier<ECSComponent> componentSupplier, Supplier<ECSSystem<? extends ECSComponent>> systemSupplier) {
            this.componentSupplier = componentSupplier;
            this.systemSupplier = systemSupplier;
        }

        public ECSSystem<? extends ECSComponent> createSystem() {
            return (ECSSystem<? extends ECSComponent>) systemSupplier.get();
        }

        public ECSComponent createComponent() {
            return componentSupplier.get();
        }
    }


    /* -------------- Getters -------------- */
    List<E> getComponentList();

    Set<E> componentSet();

    E getComponent();

    int getWorkflowMask();

    /* -------------- Setters -------------- */
    void componentList(List<E> componentList);

    void componentSet(Set<E> componentSet);

    void setComponent(E component);


    /* -------------- Other methods -------------- */
    void addComponent(E component);

    E getComponent(E component);

    void removeComponent(E component);

    Entity getEntityByName(String name);
}
