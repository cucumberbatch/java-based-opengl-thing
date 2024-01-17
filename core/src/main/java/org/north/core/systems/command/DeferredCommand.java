package org.north.core.systems.command;

public interface DeferredCommand {
    enum CommandType {
        ADD_COMPONENT, REMOVE_COMPONENT
    }

    CommandType getType();
}