package org.north.core.system.process;

import org.north.core.component.Component;

public interface UpdateProcess<ComponentInstance extends Component> {

    /**
     * An interface method that updates all the component content
     *
     * @param component is a component on which it needs to perform update
     * @param deltaTime is a time interval between present and previous frames
     * @author cucumberbatch
     */
    default void update(ComponentInstance component, float deltaTime) {}

}
