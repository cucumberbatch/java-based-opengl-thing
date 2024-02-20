package org.north.core.system.process;

import org.north.core.component.Component;

public interface UpdateProcess<C extends Component> {

    /**
     * An interface method that updates all the component content
     *
     * @param deltaTime is a time interval between present and previous frames
     * @author cucumberbatch
     */
    default void update(C component, float deltaTime) {}

}
