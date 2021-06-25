package ecs.systems;

import ecs.components.*;
import ecs.entities.Entity;
import ecs.systems.processes.ISystem;

import java.util.List;
import java.util.Set;

public interface ECSSystem<E extends ECSComponent> extends ISystem, IComponentManager {

    /* Type of system */
    enum Type {
        RENDERER {
            @Override
            public ECSSystem createSystem() {
                return new RendererSystem();
            }

            @Override
            public ECSComponent createComponent() {
                return new Renderer();
            }
        },
        TRANSFORM {
            @Override
            public ECSSystem createSystem() {
                return new TransformSystem();
            }

            @Override
            public ECSComponent createComponent() {
                return new Transform();
            }
        },
        RIGIDBODY {
            @Override
            public ECSSystem createSystem() {
                return new RigidBodySystem();
            }

            @Override
            public ECSComponent createComponent() {
                return new RigidBody();
            }
        },
        CAMERA {
            @Override
            public ECSSystem createSystem() {
                return new CameraSystem();
            }

            @Override
            public ECSComponent createComponent() {
                return new Camera();
            }
        },
        PLANE {
            @Override
            public ECSSystem createSystem() {
                return new PlaneRendererSystem();
            }

            @Override
            public ECSComponent createComponent() {
                return new PlaneRenderer();
            }
        };

        public abstract ECSSystem createSystem();
        public abstract ECSComponent createComponent();
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

    void setWorkflowMask(int mask);

    /* -------------- Other methods -------------- */
    void addComponent(E component);

    E getComponent(E component);

    void removeComponent(E component);

    Entity getEntityByName(String name);
}
