package ecs.entities;

import ecs.Engine;
import ecs.Scene;
import ecs.components.AbstractComponent;
import ecs.components.Component;
import ecs.systems.AbstractSystem;
import ecs.managment.factory.ComponentSystemFactory;
import org.junit.jupiter.api.Test;

class EngineTest {

    @Test
    void testEngineWorkflow() {
        Scene scene     = new Scene();
        Engine engine   = new Engine(scene);
        Entity object   = new Entity(engine, scene);

        class MyComponent extends AbstractComponent {
            float value;
        }

        class MySystem extends AbstractSystem {
            @Override
            public void update(float deltaTime) {
                ((MyComponent) currentComponent()).value++;
            }
        }

        ComponentSystemFactory<Component> componentFactory = type -> new MyComponent();



    }

}