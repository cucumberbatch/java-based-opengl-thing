package ecs.components;

public class InitEntities extends AbstractComponent {
    public InitEntities() {
        this.setState(READY_TO_INIT_STATE);
    }
}
