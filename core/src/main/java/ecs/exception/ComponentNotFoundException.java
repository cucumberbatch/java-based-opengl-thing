package ecs.exception;

import ecs.components.Component;

public class ComponentNotFoundException extends RuntimeException {
    public ComponentNotFoundException(long id) {
        super(String.format("Component with id: %s not found!", id));
    }

    public ComponentNotFoundException(Class<? extends Component> clazz) {
        super(String.format("Component of type: %s not found!", clazz.getSimpleName()));
    }
}
