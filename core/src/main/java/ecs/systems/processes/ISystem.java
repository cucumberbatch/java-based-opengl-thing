package ecs.systems.processes;

import ecs.graphics.Window;
import ecs.systems.Collision;

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
    default void update(float deltaTime) {}

    /**
     * An interface method that renders component content
     *
     * @param window a window handle in which render occurs
     * @author cucumberbatch
     */
    default void render(Window window) {}


    default void onCollisionStart(Collision collision) { }
    default void onCollision(Collision collision) { }
    default void onCollisionEnd(Collision collision) { }
}
