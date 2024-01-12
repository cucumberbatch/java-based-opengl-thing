package org.north.core.systems.processes;

import org.north.core.graphics.Graphics;
import org.north.core.graphics.Window;
import org.north.core.systems.Collision;

public interface ISystem {

    /**
     * An interface method that allows the executions of individual instructions
     * of the component it inherits at the first frame of after the component
     * initialization
     *
     * @throws Exception
     * @author cucumberbatch
     */
    default void init() throws RuntimeException {}

    /**
     * An interface method that allows the
     *
     * @param window
     */
    default void input(Window window) {}

    /**
     * An interface method that updates all the component content
     *
     * @param deltaTime is a time interval between present and previous frames
     * @author cucumberbatch
     */
    default void update(final float deltaTime) {}

    /**
     * An interface method that renders component content
     *
     * @param graphics a graphics api for rendering objects
     * @author cucumberbatch
     */
    default void render(Graphics graphics) {}


    default void onCollisionStart(Collision collision) { }
    default void onCollision(Collision collision) { }
    default void onCollisionEnd(Collision collision) { }
}
