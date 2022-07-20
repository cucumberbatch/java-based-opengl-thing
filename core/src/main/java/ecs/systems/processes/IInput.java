package ecs.systems.processes;

import ecs.gl.Window;


public interface IInput {
    default void input(Window window) {
    }
}
