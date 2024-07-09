package org.north.core.system.process;

import org.north.core.component.Component;
import org.north.core.system.Input;

public interface InputHandleProcess<ComponentInstance extends Component> extends Process {

    /**
     * An interface method that helps to handle input events for selected component
     *
     * @param component is a component on which it needs to perform update
     * @param input is an input context that contains an input events
     * @author cucumberbatch
     */
    void handleInput(ComponentInstance component, Input input);
}
