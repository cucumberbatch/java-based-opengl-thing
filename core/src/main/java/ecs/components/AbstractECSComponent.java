package ecs.components;

import ecs.entities.Entity;
import ecs.systems.ECSSystem;
import ecs.utils.Logger;

import java.util.Arrays;
import java.util.UUID;

public abstract class AbstractECSComponent implements ECSComponent {

    public enum State {
        LATE_INIT_STATE(AbstractECSComponent.LATE_INIT_STATE),
        READY_TO_INIT_STATE(AbstractECSComponent.READY_TO_INIT_STATE),
        READY_TO_OPERATE_STATE(AbstractECSComponent.READY_TO_OPERATE_STATE);

        private byte value;

        State(byte value) {
            this.value = value;
        }

        public static State fromValue(byte value) {
            return Arrays.stream(State.values()).filter(s -> s.value == value).findFirst().orElseThrow(IllegalArgumentException::new);
        }
    }

    public static final byte LATE_INIT_STATE        = 0; // a state when needs to wait for other systems initializations
    public static final byte READY_TO_INIT_STATE    = 1; // a state when constructor is already been executed
    public static final byte READY_TO_OPERATE_STATE = 2; // a state when init method was called for each component


    /* Name of component */
    public String name;

    /* Entity which this component is belongs to */
    public Entity entity;

    /* If you need to execute a special method of the component
    you can get a reference to the system it belongs */
    public ECSSystem<? extends ECSComponent> system;

    /* Reference to the transform component of its entity */
    public Transform transform;

    /* Activity state of component */
    public boolean isActive = true;
    public UUID id;

    /* State of component lifecycle */
    private byte state = LATE_INIT_STATE;


    @Override
    public void reset() {
        entity = null;
        transform = null;
    }

    /*
     Getters and setters implementation by an abstract component class
     */
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public ECSSystem<? extends ECSComponent> getSystem() {
        return system;
    }

    @Override
    public void setSystem(ECSSystem<? extends ECSComponent> system) {
        this.system = system;
    }

    @Override
    public Transform getTransform() {
        return transform;
    }

    @Override
    public void setTransform(Transform transform) {
        this.transform = transform;
    }


    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActivity(boolean activity) {
        this.isActive = activity;
    }

    @Override
    public void switchActivity() {
        isActive = !isActive;
    }

    @Override
    public byte getState() {
        return state;
    }

    @Override
    public void setState(byte state) {
        if (this.state == state) return;
        Logger.info(String.format("Component \'%s\' state changed\n\tfrom:\t<yellow>%s</>\n\t  to:\t%s", getName(), State.fromValue(this.state), State.fromValue(state)));
        this.state = state;
    }

    // TODO: 07.06.2021 implementation isn't done yet
    @Override
    public <E extends ECSComponent> E getInstance() {
        throw new UnsupportedOperationException();
    }

    // TODO: 07.06.2021 implementation isn't done yet
    @Override
    public <E extends ECSComponent> E getReplica() {
        throw new UnsupportedOperationException();
    }
}
