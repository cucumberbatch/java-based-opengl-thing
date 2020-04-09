package ecs.entities;

import ecs.components.Component;

public interface IComponentManager {

    /**
     * This method allows you to add a components to an entity that it calls
     * @param clazz is a class of component that needs to add
     */
    <E extends Component> void AddComponent(Class<E> clazz) throws IllegalArgumentException;

    /**
     * Method that allows you to remove the component that attached to it entity
     * @param clazz is a class of component that needs to remove
     */
    <E extends Component> void RemoveComponent(Class<E> clazz) throws IllegalArgumentException;

    /**
     * This method allows you to get the concrete component to an entity that it calls
     * @param clazz is a class of component that needs to get
     */
    <E extends Component> E GetComponent(Class<E> clazz) throws IllegalArgumentException;

}
