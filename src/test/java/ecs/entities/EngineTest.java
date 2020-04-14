package ecs.entities;

import ecs.Engine;
import ecs.Scene;
import ecs.components.AbstractComponent;
import ecs.components.Component;
import ecs.systems.AbstractSystem;
import ecs.systems.System;
import ecs.util.managment.Factory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {

    @Test
    void testEngineWorkflow() {
        Scene scene = new Scene();
        Engine engine = new Engine(scene);
        Entity object = new Entity(engine, scene);

        class MyComponent extends AbstractComponent {
            float value;
        }

        class MySystem extends AbstractSystem {
            @Override
            public void update(float deltaTime) {
                ((MyComponent) currentComponent()).value++;
            }
        }

        Factory<Component> componentFactory = type -> new MyComponent();

        Factory<System> systemFactory = type -> new MySystem();



    }

}