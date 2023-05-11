package ecs.managment;

import ecs.GameLogic;
import ecs.Scene;
import ecs.gl.Window;
import ecs.graphics.Renderer2D;
import ecs.graphics.Shader;
import ecs.graphics.Texture;
import ecs.graphics.VertexArray;
import ecs.shapes.Rectangle;
import ecs.systems.Input;
import ecs.utils.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL15;
import vectors.Vector2f;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

public class BasicGameLoop implements GameLogic {

    private final Window window;


    private float[] vertices = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f,  0.5f, 0.0f,
            -0.5f,  0.5f, 0.0f
    };

    private byte[] indices = {0, 3, 1, 3, 1, 2};

    Texture texture;

    Shader shader;

    Rectangle rectangle;

    public BasicGameLoop(Window window) {
        this.window = window;
    }

    @Override
    public void run() {
        FrameTiming timingContext = new FrameTiming();

        while (window.shouldNotClose()) {
            timingContext.updateTiming();

            init();
            try {
                updateInput();
                update(timingContext.getElapsedTime());
                registerCollisions();
                handleCollisions();
                render(window);
            } catch (RuntimeException e) {
                Logger.error(e);
            }

            timingContext.sync();
        }
    }

    @Override
    public void init() throws RuntimeException {
        texture = new Texture("core/assets/textures/screen-frame-1024.png");
        shader = new Shader("core/assets/shaders/bg.vert", "core/assets/shaders/bg.frag");
        rectangle = new Rectangle(new Vector2f(10, 10), new Vector2f(30, 30));
    }

    @Override
    public void updateInput() {
        Input.updateInput();
    }

    @Override
    public void registerCollisions() {

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void handleCollisions() {

    }

    @Override
    public void render(Window window) {
        GL15.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        Renderer2D.draw(
                new VertexArray(
                        rectangle.toVertices(),
                        indices,
                        new float[]{0, 1, 0, 0, 1, 0, 1, 1}),
                texture,
                shader);

        GLFW.glfwSwapBuffers(window.getWindow());
    }

    @Override
    public void setScene(Scene scene) {
        return;
    }
}
