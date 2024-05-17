package org.north.core.system.process;

import org.north.core.component.Component;

public interface InitProcess<ComponentInstance extends Component> {

    /**
     * An interface method that allows the executions of initialize
     * instructions on the passed in component at the next frame
     * of after the component instantiation
     *
     * @param component is a component that needs to initialize
     * @throws RuntimeException if a runtime exception occurs..
     * @author cucumberbatch
     */
    default void init(ComponentInstance component) throws RuntimeException {};

}
