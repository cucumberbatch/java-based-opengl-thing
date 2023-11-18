package org.north.core.utils;

/**
 * Interface for instantiating objects
 *
 * @param <T>
 * @author cucumberbatch
 */
public interface Instantiatable<T> {
    /**
     * A method that allows the instantiating objects
     *
     * @return an instance of object that implements this interface
     */
    <E extends T> E getInstance();
}
