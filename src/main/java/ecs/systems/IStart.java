package ecs.systems;

/**
 * A system that allows the executions of individual instructions
 * of the component it inherits at the first frame of the game
 *
 * @author cucumberbatch
 */
public interface IStart extends ISystem {
    /**
     *
     */
    default void start() {};
}
