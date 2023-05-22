package ecs.graphics;

import ecs.graphics.Shader;
import ecs.systems.Input;

import ecs.utils.Logger;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import vectors.Vector2f;

import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    public final String title;
    public static int width;
    public static int height;

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    private long window = -1;
    private final boolean vSync;

    public Window(String title, int width, int height, boolean vSync) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;
    }

    public void init() {
        if (window != -1) return;

        GLFW.glfwSetErrorCallback((code, message) -> {
            Logger.error(String.format("err_code 0x%08X: %s ", code, message));
        });

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL30.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL30.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable

        window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);

        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setting up input callback handlers
        GLFW.glfwSetKeyCallback(window, new Input.KeyboardInput());
        GLFW.glfwSetCursorPosCallback(window, new Input.CursorPositionInput());

        // Grab the cursor in window
        // glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        // Let the cursor be free
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*
            GLFW.glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            // Center the window
            GLFW.glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }
        // the stack frame is popped automatically
        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window);
        // Enable v-sync
        GLFW.glfwSwapInterval(vSync ? 1 : 0);

        GLFW.glfwShowWindow(window);
        GL.createCapabilities();


//        GL30.glClearColor(0.16f, 0.16f, 0.16f, 1.0f);
        GL30.glClearColor(0f, 0f, 0f, 0f);
        GL30.glEnable(GL30.GL_DEPTH_TEST);

        // enable blending
        GL30.glEnable(GL30.GL_BLEND);
        GL30.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL30.glActiveTexture(GL30.GL_TEXTURE1);
        Shader.loadAll();
    }

    public void destroy() {
        if (window == -1) return;

        Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    public static Vector2f translatePointToWindow(Vector2f point) {
        return new Vector2f(
            -1 + point.x / width  * 2,
             1 - point.y / height * 2);
    }

    public long getWindow() {
        return window;
    }

    public boolean shouldNotClose() {
        return !GLFW.glfwWindowShouldClose(window);
    }
}