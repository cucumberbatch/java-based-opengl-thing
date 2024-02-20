package org.north.core.system.command;

public interface DeferredCommand {
    enum CommandType {
        ADD_COMPONENT, REMOVE_COMPONENT
    }

    CommandType getType();
}