package ecs;

import ecs.components.ECSComponent;
import ecs.entities.Entity;
import ecs.gl.Window;
import ecs.managment.factory.ComponentFactory;
import ecs.managment.factory.ComponentSystemFactory;
import ecs.managment.factory.IFactory;
import ecs.managment.factory.SystemFactory;
import ecs.managment.memory.IPool;
import ecs.managment.memory.Pool;
import ecs.systems.ECSSystem;
import ecs.systems.Input;
import ecs.systems.SystemHandler;
import ecs.systems.processes.ISystem;
import ecs.utils.Logger;
import ecs.utils.TerminalUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class Engine implements Runnable, ISystem {
    private final Window window;
    private final Engine engine = this;
    private IGameLogic gameLogic;
    private Scene scene;
    private final List<Entity> entityList = new ArrayList<>();

    private final float frameRate = 60.0f;
    private final float secondsPerFrame = 1.0f / frameRate;
    private boolean keepOnRunning;

    private final SystemHandler systemHandler = new SystemHandler();

    private final ComponentSystemFactory<ECSSystem>    systemFactory    = new SystemFactory();
    private final ComponentSystemFactory<ECSComponent> componentFactory = new ComponentFactory();
    private final IFactory<Entity>                     entityFactory    = new IFactory<Entity>() {
        private int counter;

        @Override
        public Entity create() {
            return new Entity("entity_".concat(String.valueOf(counter++)), engine, scene);
        }
    };

    /* Pool for entities, that creates  */
    private final IPool<Entity> entityPool = new Pool<>(entityFactory);


    SceneView view;


    public Engine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) {
        window = new Window(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;

       // createJFrame();

    }

    public Engine(Scene scene) {
        this.scene = scene;
        this.window = null;

       // createJFrame();
    }

    private void createJFrame() {
        view = new SceneView("Scene view");
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

        gameLoop();

        destroy();

        window.destroy();
    }

    private void gameLoop() {
        double previous = getTime();
        while (!glfwWindowShouldClose(window.getWindow())) {
            double loopStartTime = getTime();
            double elapsedTime = loopStartTime - previous;
            previous = loopStartTime;

            Logger.trace(String.format("Started game loop iteration with <bold>dT: %fs</> and <bold>%d fps</>", elapsedTime, (int) (1 / elapsedTime)));

            Input.updateInput();

            try {
                systemHandler.update((float) elapsedTime);
            } catch (Exception e) {
                Logger.error("System update exception", e);
            }

            try {
                systemHandler.render(window);
            } catch (Exception e) {
                Logger.error("System render exception", e);
            }
//            sync(loopStartTime);
        }
    }

    private void sync(double loopStartTime) {
        double loopSlot = 1.0d / 60.0d;
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
            systemHandler.addSystem(type, systemFactory.create(type));
        }
        systemHandler.linkComponentAndSystem(type, component);
    }

    @SuppressWarnings("unchecked")
    public <E extends ECSComponent> E instantiateNewComponent(ECSSystem.Type type) {
        return (E) componentFactory.create(type);
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
