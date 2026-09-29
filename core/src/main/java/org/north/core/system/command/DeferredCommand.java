package org.north.core.system.command;

import org.north.core.management.SystemManager;

@FunctionalInterface
public interface DeferredCommand {
    void execute(SystemManager systemManager);
}