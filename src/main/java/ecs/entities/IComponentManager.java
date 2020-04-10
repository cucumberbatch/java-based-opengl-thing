package ecs.entities;

import ecs.components.Component;

import java.util.function.Supplier;

public interface IComponentManager {

    /**
     * This method allows you to add a components to an entity that it calls
     * @param supplier a class of component that needs to be added to
     */
    <E extends Component> void AddComponent(Supplier<E> supplier) throws IllegalArgumentException;

    /**
     * This method allows you to get the concrete component to an entity that it calls
     * @param supplier a class of the component that needs to get
     * @return the component that contains in entity
     */
    <E extends Component> E GetComponent(Supplier<E> supplier) throws IllegalArgumentException;

    /**
     * Method that allows you to remove the component that attached to it entity
     * @param supplier is a class of component that needs to remove
     * @return the component that contains in entity and remove it from the component collection
     */
    <E extends Component> E RemoveComponent(Supplier<E> supplier) throws IllegalArgumentException;

}
