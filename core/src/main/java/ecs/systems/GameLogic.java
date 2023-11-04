package ecs.systems;

import ecs.scene.Scene;
import ecs.graphics.Window;
import ecs.systems.processes.ISystem;

public interface GameLogic extends Runnable, ISystem {
    void init() throws RuntimeException;
    void updateInput();
    void registerCollisions();
    void update(float deltaTime);
    void handleCollisions();
    void render(Window window);

    void setScene(Scene scene);
}
