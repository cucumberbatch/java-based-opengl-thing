package org.north.core.graphics;

import org.north.core.config.EngineConfig;
import org.north.core.physics.collision.MeshMovementListener;
import org.north.core.reflection.di.Inject;
import org.north.core.system.CameraControlsSystem;
import org.north.core.system.Input;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryStack;
import org.joml.Vector2f;
import org.north.core.utils.logger.LoggerFactory;

import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    public final String title;
    private static int width;
    private static int height;

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    private long window = -1;
    private final boolean vSync;

    private static final Logger log = LoggerFactory.createLogger(Window.class);

    @Inject
    public Window(EngineConfig config) {
        this(config.windowTitle, config.windowWidth, config.windowHeight, config.vsync);
    }

    public Window(String title, int width, int height, boolean vSync) {
        this.width = width;
        this.height = height;
        this.title = title;
        this.vSync = vSync;
    }

    public void init() {
        log.info("Window initialization started");
        if (window != -1) return;

        GLFW.glfwSetErrorCallback((code, message) -> log.log(Level.SEVERE, "err_code 0x%08X: %s ", new Object[]{code, message}));

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints(); // optional, the current window hints are already the default
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // the window will stay hidden after creation
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE); // the window will be resizable

        window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);

        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setting up input callback handlers
        GLFW.glfwSetKeyCallback(window, new Input.KeyboardInput());
        GLFW.glfwSetMouseButtonCallback(window, new Input.MouseInput());
        GLFW.glfwSetCursorPosCallback(window, new Input.CursorPositionInput());
        GLFW.glfwSetWindowSizeCallback(window, (window, width, height) -> new CameraControlsSystem.WindowSizeCallback(null));



//        GLFW.glfwSetWindowRefreshCallback(window, new GLFWWindowRefreshCallback() {
//            @Override
//            public void invoke(long l) {
//                 log.info(String.format("Refreshed window[%s] value[%s]", window, l));
//            }
//        });

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*
            GLFW.glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            if (videoMode == null)
                throw new RuntimeException("Video mode is not found!");

            // Center the window
            GLFW.glfwSetWindowPos(
                    window,
                    (videoMode.width() - pWidth.get(0)) / 2,
                    (videoMode.height() - pHeight.get(0)) / 2
            );
        }
        // the stack frame is popped automatically
        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(window);
        // Enable v-sync
        GLFW.glfwSwapInterval(vSync ? 1 : 0);

        GLFW.glfwShowWindow(window);
        GL.createCapabilities();


//        GL30.glClearColor(0f, 0f, 0f, 1f);
        GL30.glClearColor(1f, 1f, 1f, 1f);

        GL30.glClearDepth(1f);
        GL30.glDepthRange(0f, 1f);
        GL30.glEnable(GL30.GL_DEPTH_TEST);
        GL30.glDepthFunc(GL11.GL_LEQUAL);

        // enable blending
        GL30.glEnable(GL30.GL_BLEND);
        GL30.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        log.info("Window initialization ended");
    }

    public void destroy() {
        log.info("Window destruction process started");

        if (window == -1) return;

        Callbacks.glfwFreeCallbacks(window);
        GLFW.glfwDestroyWindow(window);

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();

        // when we hit an exit button in application we need to stop all threads
        MeshMovementListener.shutdownThreadExecution();

        log.info("Window destruction process ended");
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
