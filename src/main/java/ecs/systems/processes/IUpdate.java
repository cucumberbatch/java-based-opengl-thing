package ecs.systems.processes;

import ecs.components.Component;

/**
 * A system that updates all the component content
 *
 * @author cucumberbatch
 */
public interface IUpdate extends IProcess {
    /**
     * @param deltaTime is a time interval between present and previous frames
     */
    default void update(float deltaTime) {
    }
}
