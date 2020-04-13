package ecs.util;

/**
 * Interface for replicating objects
 *
 * @param <T>
 * @author cucumberbatch
 */
public interface Replicable<T> {
    /**
     * A method that allows making copy of objects
     *
     * @return a copy of object that implements this interface
     */
    <E extends T> E getReplica();
}
