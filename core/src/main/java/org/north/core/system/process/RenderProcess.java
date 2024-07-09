package org.north.core.system.process;

import org.north.core.component.Component;
import org.north.core.graphics.Graphics;

public interface RenderProcess<ComponentInstance extends Component> extends Process {

    /**
     * An interface method that renders component content
     *
     * @param graphics a graphics api for rendering objects
     * @author cucumberbatch
     */
    void render(ComponentInstance component, Graphics graphics);

}
