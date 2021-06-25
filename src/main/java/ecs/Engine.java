package ecs;

import ecs.components.ECSComponent;
import ecs.entities.Entity;
import ecs.graphics.gl.Window;
import ecs.managment.factory.IFactory;
import ecs.managment.memory.IPool;
import ecs.managment.memory.Pool;
import ecs.systems.ECSSystem;
import ecs.graphics.Graphics;
import ecs.systems.Input;
import ecs.systems.SystemHandler;
import ecs.utils.TerminalUtils;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Engine implements Runnable {
    private final Thread gameLoopThread;
    private final Window window;
    private final Engine engine = this;
    private Scene scene;
    private final List<Entity> entityList = new ArrayList<>();

    private float frameRate = 60.0f;
    private final float secondsPerFrame = 1.0f / frameRate;
    private boolean keepOnRunning;

    private final SystemHandler systemHandler = new SystemHandler();

    private final IFactory<Entity>                     entityFactory    = new IFactory<>() {
        private long counter;

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

    public void init() {
        // initialize components in loop
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

    public void destroy() {
        // destroy components and remove every system from destruction handler
        systemHandler.destroy();
    }

    // ---------------------  Running method for main game engine thread  ---------------------------------------

    // TODO: needs to understand what I want here...
    @Override
    public void run() {
        assert window != null;
        window.init();


        gameLoop();

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

            // component initialization process
            init();

            // component deletion process
            destroy();

            // input registration
            handleInput();

            // component logic update process
            update((float) elapsedTime);

            sync(loopStartTime);

            // component rendering process
            render(window);

            // show entities scene hierarchy
            showHierarchy();

            System.out.println("elapsed time: " + elapsedTime + "\tFPS: " + (int) (1 / elapsedTime));
        }
    }

    private void handleInput() {
        Input.updateInput();
    }

    private void sync(double loopStartTime) {
        double loopSlot = 1.0d / frameRate;
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
        return System.currentTimeMillis() * 0.001;
    }

    // ---------------------  Component access implementation  --------------------------------------------------

    public void addComponentToSystem(ECSSystem.Type type, ECSComponent component) {
        systemHandler.addSystem(type);
        systemHandler.linkComponentAndSystem(type, component);
    }

    @SuppressWarnings("unchecked")
    public <E extends ECSComponent> E instantiateNewComponent(ECSSystem.Type type) {
        return (E) type.createComponent();
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

    public void showHierarchy() {
        String accumulator = "";

        for (Entity e : entityList) {
            if (e.parent == null) {
                accumulator = accumulator + e.showHierarchy(0);
            }
        }

//        view.updateTextField(TerminalUtils.deleteAnsiCodes(accumulator));
        view.updateTextField(systemHandler.getSystemsStatus());
    }
}
