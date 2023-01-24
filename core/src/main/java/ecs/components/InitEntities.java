package ecs.components;

public class InitEntities extends AbstractECSComponent {
    public InitEntities() {
        this.setState(READY_TO_INIT_STATE);
    }
}
