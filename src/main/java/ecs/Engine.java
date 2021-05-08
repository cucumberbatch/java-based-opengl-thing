package ecs;

import ecs.components.Component;
import ecs.entities.Entity;
import ecs.gl.Window;
import ecs.managment.factory.ComponentFactory;
import ecs.managment.factory.ComponentSystemFactory;
import ecs.managment.factory.IFactory;
import ecs.managment.factory.SystemFactory;
import ecs.managment.memory.Pool;
import ecs.managment.memory.IPool;
import ecs.systems.System;
import ecs.systems.SystemHandler;
import ecs.systems.processes.ISystem;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class Engine implements Runnable, ISystem {
    private final Thread gameLoopThread;
    private final Window window;
    private final Engine engine = this;
    private IGameLogic gameLogic;
    private Scene scene;
    private final List<Entity> entityList = new ArrayList<>();

    private final float frameRate = 50.0f;
    private final float secondsPerFrame = 1.0f / frameRate;
    private boolean keepOnRunning;

    private final SystemHandler s_handler = new SystemHandler();

    private final ComponentSystemFactory<System> s_factory = new SystemFactory();
    private final ComponentSystemFactory<Component> c_factory = new ComponentFactory();

    /* Pool for entities, that creates  */
    private final IPool<Entity> e_I_pool = new Pool<>(new IFactory<Entity>() {
        @Override
        public Entity create() {
            return new Entity(engine, scene);
        }
    });


    public Engine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) {
        gameLoopThread = new Thread(this, "GAME_ENGINE_LOOP");
        window = new Window(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
    }

    public Engine(Scene scene) {
        this.scene = scene;
        this.gameLoopThread = new Thread();
        this.window = null;
    }

    public void addEntity(Entity entity) {
        entityList.add(entity);
    }

    public void deactivateEntity(Entity entity) {
        entity.reset();
        e_I_pool.put(entity);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    // ---------------------  Game engine processes  -----------------------------------------------------------

    public void init() throws Exception {
        s_handler.init();
    }

    public void input() {
        gameLogic.input(window);
    }

    public void update(float deltaTime) {
        s_handler.update(deltaTime);
    }

    public void render(Window window) {
        s_handler.render(window);
    }

    public void destroy() {

    }

    // ---------------------  Running method for main game engine thread  ---------------------------------------

    // TODO: needs to understand what I want here...
    @Override
    public void run() {
        assert window != null;
        window.init();

        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        gameLoop();

        destroy();

        window.destroy();
    }

    private void gameLoop() {
        double previous = getTime();
        double steps = 0.0;
        while (!glfwWindowShouldClose(window.getWindow())) {
            double loopStartTime = getTime();
            double elapsedTime = loopStartTime - previous;
            previous = loopStartTime;
            steps += elapsedTime;

            handleInput();

            while (steps >= secondsPerFrame) {
                update((float) elapsedTime);
                steps -= secondsPerFrame;
            }

            render(window);
            sync(loopStartTime);

        }
    }

    private void updateGameState() {

    }

    private void handleInput() {

    }

    private void sync(double loopStartTime) {
        double loopSlot = 1.0d / 50.0d;
        double endTime = loopStartTime + loopSlot;
        while (getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private double getTime() {
        return (double) java.lang.System.currentTimeMillis() / 1_000L;
    }

    // ---------------------  Component access implementation  --------------------------------------------------

    public void addComponentToSystem(System.Type type, Component component) {
        if (!s_handler.hasSystem(type)) {
            s_handler.addSystem(type, s_factory.create(type));
        }
        s_handler.linkComponentAndSystem(type, component);
    }

    public <E extends Component> E instantiateNewComponent(System.Type type) {
        return (E) c_factory.create(type);
    }

//    public Component getComponentFromSystem(ComponentType type) {
//        return s_handler.getSystemByComponentType(type);
//    }

    public <E extends Component> E removeComponentFromSystem(System.Type type, E component) {
        s_handler.removeComponent(type, component);
        return component;
    }

    public Window window() {
        return window;
    }
}
