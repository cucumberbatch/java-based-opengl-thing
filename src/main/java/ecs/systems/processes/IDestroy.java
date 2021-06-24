package ecs.systems.processes;

/**
 * Interface that helps control component destruction process
 *
 * @author cucumberbatch
 */
public interface IDestroy extends IProcess {
    default void onDestroy() {
    }
}
