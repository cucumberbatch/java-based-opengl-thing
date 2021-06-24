package ecs.systems.processes;

/**
 * Interface that allows the executions of individual instructions
 * of the component it inherits at the first frame of the game
 *
 * @author cucumberbatch
 */
public interface IInit extends IProcess {
    default void onInit() {
    }
}
