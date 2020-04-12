package ecs.components;

public enum ComponentType {
    TRANSFORM(new Transform()),
    RIGIDBODY(new RigidBody());

    private Component component;

    ComponentType(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }
}
