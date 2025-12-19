package org.north.core.component;

import org.north.core.architecture.entity.Entity;

import java.util.UUID;

public abstract class AbstractComponent implements Component {

    /* Entity which this component is belongs to */
    public transient Entity entity;

    /* Activity state of component */
    public boolean isActive = true;

    /* State of component lifecycle */
    private ComponentState state = ComponentState.READY_TO_INIT_STATE;


    /*
     Getters and setters implementation by an abstract component class
     */
    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
        if (this instanceof Transform) {
            this.entity.transform = (Transform) this;
        }
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
    public ComponentState getState() {
        return state;
    }

    @Override
    public void setState(ComponentState state) {
        if (state == null) throw new NullPointerException("Component state cannot be null!");
        if (this.state == state) return;
        this.state = state;
    }

    @Override
    public String toString() {
        return "AbstractComponent{" +
                "entityId=" + entity.id +
                ", isActive=" + isActive +
                ", state=" + state.name() +
                '}';
    }

}
