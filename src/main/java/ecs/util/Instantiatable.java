package ecs.util;

/**
 * Interface for instantiating objects
 *
 * @param <T>
 */
public interface Instantiatable<T> {
    /**
     * A method that allows the instantiating objects
     *
     * @return an instance of object that implements this interface
     */
    T getInstance();
}
