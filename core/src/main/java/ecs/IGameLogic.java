package ecs;

import ecs.components.ECSComponent;
import ecs.gl.Window;
import ecs.systems.ECSSystem;
import ecs.systems.SystemHandler;
import ecs.systems.processes.ISystem;

public interface IGameLogic extends ISystem {

    // default cycle methods
    void init() throws Exception;
    void registerCollisions();
    void update(float deltaTime);
    void handleCollisions();
    void render(Window window);

    // specific architecture methods
    ECSSystem<?> getSystem(ECSSystem.Type type);
    void addSystem(ECSSystem.Type type, ECSSystem<?> system);
    boolean hasSystem(ECSSystem.Type type);
    void addEntityComponentInitPair(SystemHandler.InitComponentPair pair);
    void linkComponentAndSystem(ECSSystem.Type type, ECSComponent component);
    void removeComponent(ECSSystem.Type type, ECSComponent component);

}
