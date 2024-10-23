package org.north.core.system.command;

import org.north.core.management.SystemManager;

public interface DeferredCommand {
    enum CommandType {
        ADD_COMPONENT, REMOVE_COMPONENT
    }

    void execute(SystemManager systemManager);

    CommandType getType();
}