package org.north.core.architecture.entity;

import org.north.core.component.Component;

/**
 * Interface for an entity components behaviour
 *
 * @author cucumberbatch
 */
public interface IComponentManager<E extends Component> {

    /**
     * This method allows you to add a components to an entity that it calls
     *
     * @param component is a component that needs to be added to
     * @throws IllegalArgumentException if a bad type argue, is not a component type
     * @throws ClassCastException       if unable to cast a component
     */
    void addComponent(E component) throws IllegalArgumentException, ClassCastException;

    /**
     * This method allows you to get the concrete component to an entity that it calls
     *
     * @param componentClass a class of the component that needs to get
     * @return the component that contains in entity
     * @throws IllegalArgumentException if a bad type argue, is not a component type
     * @throws ClassCastException       if unable to cast a component
     */
    E getComponent(Class<E> componentClass) throws IllegalArgumentException, ClassCastException;

    /**
     * Method that allows you to remove the component that attached to it entity
     *
     * @param componentClass is a class of component that needs to remove
     * @return the component that contains in entity and remove it from the component collection
     * @throws IllegalArgumentException if a bad type argue, is not a component type
     * @throws ClassCastException       if unable to cast a component
     */
    E removeComponent(Class<E> componentClass) throws IllegalArgumentException, ClassCastException;

}
