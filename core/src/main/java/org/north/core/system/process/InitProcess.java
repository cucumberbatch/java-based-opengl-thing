package org.north.core.system.process;

import org.north.core.component.Component;

public interface InitProcess<C extends Component> {

    /**
     * An interface method that allows the executions of individual instructions
     * of the component it inherits at the first frame of after the component
     * initialization
     *
     * @throws RuntimeException if a runtime exception occurs..
     * @author cucumberbatch
     */
    default void init(C component) throws RuntimeException {};

}
