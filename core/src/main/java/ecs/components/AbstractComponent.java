package ecs.components;

import ecs.entities.Entity;
import ecs.utils.Logger;

import java.util.Arrays;

public abstract class AbstractComponent implements Component {

    public enum State {
        LATE_INIT_STATE(AbstractComponent.LATE_INIT_STATE),
        READY_TO_INIT_STATE(AbstractComponent.READY_TO_INIT_STATE),
        READY_TO_OPERATE_STATE(AbstractComponent.READY_TO_OPERATE_STATE);

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

    public long id;
    public String name;

    /* Entity which this component is belongs to */
    public Entity entity;

    /* Reference to the transform component of its entity */
    public Transform transform;

    /* Activity state of component */
    public boolean isActive = true;

    /* State of component lifecycle */
    private byte state = READY_TO_INIT_STATE;


    @Override
    public void reset() {
        entity = null;
        transform = null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Component> T getInstance() {
        return (T) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Component> T getReplica() {
        return (T) this;
    }


    /*
     Getters and setters implementation by an abstract component class
     */
    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(int id) {
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
        Logger.info(String.format("Component state changed [id=%d type=%s]\n\tfrom:\t<yellow>%s</>\n\t  to:\t%s", getId(), getClass().getSimpleName(), State.fromValue(this.state), State.fromValue(state)));
        this.state = state;
    }

}
