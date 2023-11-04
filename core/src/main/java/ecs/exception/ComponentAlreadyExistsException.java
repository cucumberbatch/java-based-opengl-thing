package ecs.exception;

import ecs.components.Component;

public class ComponentAlreadyExistsException extends RuntimeException {
    public ComponentAlreadyExistsException(Class<? extends Component> clazz) {
        super(String.format("Component of type: %s already exists!", clazz.getSimpleName()));
    }
}
