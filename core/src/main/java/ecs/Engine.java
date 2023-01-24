package ecs;

import ecs.components.ECSComponent;
import ecs.entities.Entity;
import ecs.gl.Window;
import ecs.managment.factory.IFactory;
import ecs.managment.memory.IPool;
import ecs.managment.memory.Pool;
import ecs.systems.ECSSystem;
import ecs.systems.Input;
import ecs.systems.SystemHandler;
import ecs.systems.processes.ISystem;
import ecs.utils.Logger;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

//todo:
// * relative position (to parent entities)
// * create serialization/deserialization flow for scenes
// * add stateful animation controller
// * add gamepad support (also xinput https://github.com/StrikerX3/JXInput)
public class Engine implements Runnable, ISystem {
    private static Engine engine;
    private final Window window;
    private IGameLogic gameLogic;
    private Scene scene;
    private final List<Entity> entityList = new ArrayList<>();

    private final float frameRate = 60.0f;
    private final float secondsPerFrame = 1.0f / frameRate;
    private boolean keepOnRunning;

    private final SystemHandler systemHandler = new SystemHandler();

    public void addComponent(Entity entity, ECSSystem.Type type) {
        this.systemHandler.addEntityComponentInitPair(new SystemHandler.InitComponentPair(entity, type));
    }

    private final IFactory<Entity> entityFactory = new IFactory<Entity>() {
        private int counter;

        @Override
        public Entity create() {
            return new Entity("entity_".concat(String.valueOf(counter++)), engine, scene);
        }
    };

    /* Pool for entities, that creates  */
    private final IPool<Entity> entityPool = new Pool<>(0, entityFactory);


    SceneView view;

    public static Engine getInstance() {
        if (engine == null) {
            engine = new Engine("Default", 640, 480, true);
        }
        return engine;
    };

    public Engine(String windowTitle, int width, int height, boolean vSync) {
        Logger.info("Initializing engine..");
        window = new Window(windowTitle, width, height, vSync);

        if (vSync) {
            Logger.info("VSync is turned on!");
        } else {
            Logger.warn("VSync is turned off! Be sure that you are using frame sync!");
        }

        engine = this;
    }

    public void addEntity(Entity entity) {
        entityList.add(entity);
    }

    public Entity generateNewEntity() {
        return entityFactory.create();
    }

    public void deactivateEntity(Entity entity) {
        entity.reset();
        entityPool.put(entity);
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    // ---------------------  Game engine processes  -----------------------------------------------------------

    public void destroy() {

    }

    // ---------------------  Running method for main game engine thread  ---------------------------------------

    // TODO: needs to understand what I want here...
    @Override
    public void run() {
        assert window != null;
        window.init();

        try {
            systemHandler.init();
        } catch (Exception e) {
            Logger.error("System init exception", e);
        }

        try {
            gameLoop();
        } catch (RuntimeException e) {
            Logger.error(e);
        }

        destroy();

        window.destroy();
    }

    private void gameLoop() {
        double previous = getTime();
        double loopStartTime;
        double elapsedTime;
        while (!glfwWindowShouldClose(window.getWindow())) {
            loopStartTime = getTime();
            elapsedTime = loopStartTime - previous;
            previous = loopStartTime;

            Logger.trace(String.format("Started game loop iteration with <bold>dT: %.3f[s]</> and <bold>%d fps</>", elapsedTime, (int) (1 / elapsedTime)));

            Input.updateInput();

            systemHandler.registerCollisions();

            try {
                systemHandler.update((float) elapsedTime);
            } catch (Exception e) {
                Logger.error("System update exception", e);
            }

            systemHandler.handleCollisions();

            try {
                systemHandler.render(window);
            } catch (Exception e) {
                Logger.error("System render exception", e);

            }

            sync(loopStartTime);
        }
    }

    private void sync(double loopStartTime) {
        double loopSlot = 1.0d / 66.0d;
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

    public Entity getEntityByName(String name) {
        Entity foundEntity = null;
        for (Entity entity : entityList) {
            if (entity.name.equals(name)) {
                foundEntity = entity;
            }
        }
        return foundEntity;
    }

    public void addComponentToSystem(ECSSystem.Type type, ECSComponent component) {
        if (!systemHandler.hasSystem(type)) {
            systemHandler.addSystem(type, type.createSystem());
        }
        systemHandler.linkComponentAndSystem(type, component);
    }

    @SuppressWarnings("unchecked")
    public <E extends ECSComponent> E instantiateNewComponent(ECSSystem.Type type) {
        return (E) type.createComponent();
//        return (E) componentFactory.create(type);
    }

//    public Component getComponentFromSystem(ComponentType type) {
//        return s_handler.getSystemByComponentType(type);
//    }

    public <E extends ECSComponent> E removeComponentFromSystem(ECSSystem.Type type, E component) {
        systemHandler.removeComponent(type, component);
        return component;
    }

    public Window window() {
        return window;
    }

    public String showHierarchy() throws UnsupportedEncodingException {
        String accumulator = "";

        for (Entity e : entityList) {
            if (e.parent == null) {
                accumulator = accumulator + e.showHierarchy(0);
            }
            // view.addEntityInfo(e);
        }

        return accumulator;

        // view.updateTextField(TerminalUtils.deleteAnsiCodes(accumulator));
    }
}
