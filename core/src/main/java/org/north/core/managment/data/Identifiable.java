package org.north.core.managment.data;

/**
 * This interface marks inherited classes as identifiable,
 * which means that any instantiated object of that type
 * must have a unique id.
 *
 * @param <ID> a type of identifiers
 */
public interface Identifiable<ID> {
    /**
     * Get an id of that specific instance.
     *
     * @return identifier of that instance
     */
    ID getId();

    /**
     * Set an id for specific instance.
     * <p><b>Note:</b> must be used only single time in a whole instance lifecycle.</p>
     *
     * @param id specified identifier
     */
    void setId(ID id);
}
