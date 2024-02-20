package org.north.core.system.process;

import org.north.core.component.Component;
import org.north.core.graphics.Graphics;

public interface RenderProcess<C extends Component> {

    /**
     * An interface method that renders component content
     *
     * @param graphics a graphics api for rendering objects
     * @author cucumberbatch
     */
    default void render(C component, Graphics graphics) {}

}
