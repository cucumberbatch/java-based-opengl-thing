package ecs.systems.processes;

import ecs.gl.Window;

public interface IRender extends IProcess {

    default void render(Window window) {
    }
}
