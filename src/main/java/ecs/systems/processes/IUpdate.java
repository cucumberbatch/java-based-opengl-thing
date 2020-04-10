package ecs.systems.processes;

/**
 * A system that updates all the component content
 *
 * @author cucumberbatch
 */
public interface IUpdate extends IProcess {
    /**
     * @param deltaTime is a time interval between present and previous frames
     */
    default void update(double deltaTime) {
    }
}
