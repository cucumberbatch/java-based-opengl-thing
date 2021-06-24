package ecs.systems.processes;

import ecs.graphics.gl.Window;

public interface IRender extends IProcess {

    default void render(Window window) {
    }
}
