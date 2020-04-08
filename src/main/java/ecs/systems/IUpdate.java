package ecs.systems;

/**
 * A system that updates all the component content
 *
 * @author cucumberbatch
 */
public interface IUpdate extends ISystem {
    /**
     *
     * @param deltaTime
     */
    default void update(double deltaTime) {};
}
