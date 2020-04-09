package ecs.systems;

/**
 * A system that updates all the component content
 *
 * @author cucumberbatch
 */
public interface IUpdate extends ISystem {
    /**
     *
     * @param deltaTime is a time interval between present and previous frames
     */
    default void update(double deltaTime) {};
}
