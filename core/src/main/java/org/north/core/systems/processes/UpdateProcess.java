package org.north.core.systems.processes;

public interface UpdateProcess {

    /**
     * An interface method that updates all the component content
     *
     * @param deltaTime is a time interval between present and previous frames
     * @author cucumberbatch
     */
    default void update(float deltaTime) {}

}
