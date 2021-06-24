package ecs;

import ecs.components.ECSComponent;
import ecs.entities.Entity;
import ecs.graphics.gl.Window;
import ecs.managment.factory.ComponentFactory;
import ecs.managment.factory.ComponentSystemFactory;
import ecs.managment.factory.IFactory;
import ecs.managment.factory.SystemFactory;
import ecs.managment.memory.IPool;
import ecs.managment.memory.Pool;
import ecs.systems.ECSSystem;
import ecs.graphics.Graphics;
import ecs.systems.Input;
import ecs.systems.SystemHandler;
import ecs.systems.processes.ISystem;
import ecs.utils.TerminalUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

public class Engine implements Runnable, ISystem {
    private final Thread gameLoopThread;
    private final Window window;
    private final Engine engine = this;
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


    public Engine(String windowTitle, int width, int height, boolean vSync) {
        gameLoopThread = new Thread(this, "GAME_ENGINE_LOOP");
        window = new Window(windowTitle, width, height, vSync);

        createJFrame();

    }

    public Engine(Scene scene) {
        this.scene = scene;
        this.gameLoopThread = new Thread();
        this.window = null;

        createJFrame();
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

    public void init() throws Exception {
        systemHandler.init();
    }

    public void update(float deltaTime) {
        // update changes of window
        glfwPollEvents();

        // update systems
        systemHandler.update(deltaTime);
    }

    public void render(Window window) {
        // clear buffer in window screen
        Graphics.clearScreen();

        // render game graphics
        systemHandler.render(window);

        // switch screen to rendered buffer
        Graphics.swapBuffers(window);
    }

    // TODO: implement destroy method in systemHandler and add an onDestroy method
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


            update((float) elapsedTime);

//            while (steps >= secondsPerFrame) {
//                steps -= secondsPerFrame;
//            }

            render(window);
//            sync(loopStartTime);
//            System.out.println((int) (1 / elapsedTime));

            try {
                showHierarchy();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput() {
        Input.updateInput();
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

    public void showHierarchy() throws UnsupportedEncodingException {
        String accumulator = "";

        for (Entity e : entityList) {
            if (e.parent == null) {
                accumulator = accumulator + e.showHierarchy(0);
            }
        }

        view.updateTextField(TerminalUtils.deleteAnsiCodes(accumulator));
    }
}
