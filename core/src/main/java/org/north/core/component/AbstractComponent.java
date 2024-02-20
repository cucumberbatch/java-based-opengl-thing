package org.north.core.component;

import org.north.core.architecture.entity.Entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import static org.north.core.utils.SerializationUtils.readUUID;
import static org.north.core.utils.SerializationUtils.writeUUID;

public abstract class AbstractComponent implements Component, Cloneable {

    public UUID id;

    /* Entity which this component is belongs to */
    public transient Entity entity;

    /* Activity state of component */
    public boolean isActive = true;

    /* State of component lifecycle */
    private ComponentState state = ComponentState.READY_TO_INIT_STATE;


    @Override
    public void reset() {
        entity = null;
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
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Transform getTransform() {
        return entity.transform;
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
    public ComponentState getState() {
        return state;
    }

    @Override
    public void setState(ComponentState state) {
        if (state == null) throw new NullPointerException("Component state cannot be null!");
        if (this.state == state) return;
        // Logger.debug(String.format("Component state changed [id=%d type=%s]\n\tfrom:\t<yellow>%s</>\n\t  to:\t%s",
//                getId(), getClass().getSimpleName(), this.state.name(), state.name()));
        this.state = state;
    }

    @Override
    public boolean inState(ComponentState state) {
        return this.state.equals(state);
    }

    @Override
    public AbstractComponent clone() {
        try {
            AbstractComponent clone = (AbstractComponent) super.clone();
            clone.id = this.id;
            clone.entity = this.entity;
            clone.isActive = this.isActive;
            clone.state = this.state;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public String toString() {
        return "AbstractComponent{" +
                "id=" + id +
                ", isActive=" + isActive +
                ", state=" + state.name() +
                '}';
    }

    @Override
    public void serializeObject(ObjectOutputStream out) throws IOException {
        writeUUID(out, id);
        out.writeBoolean(this.isActive);
        out.writeObject(this.state);
    }

    @Override
    public void deserializeObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.id = readUUID(in);
        this.isActive = in.readBoolean();
        this.state = (ComponentState) in.readObject();
    }

}
