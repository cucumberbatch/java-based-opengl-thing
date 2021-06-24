package ecs.systems.processes;

/**
 * Interface that helps update all the component content
 *
 * @author cucumberbatch
 */
public interface IUpdate extends IProcess {
    /**
     * @param deltaTime is a time interval between present and previous frames
     */
    default void onUpdate(float deltaTime) {
    }
}
