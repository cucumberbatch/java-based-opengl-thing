package org.north.core.systems;

import org.north.core.scene.Scene;
import org.north.core.graphics.Window;
import org.north.core.systems.processes.ISystem;

public interface GameLogic extends Runnable, ISystem {
    void init() throws RuntimeException;
    void updateInput();
    void registerCollisions();
    void update(final float deltaTime);
    void handleCollisions();
    void render(Window window);

    void setScene(Scene scene);
}
