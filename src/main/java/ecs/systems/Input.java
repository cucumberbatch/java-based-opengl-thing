package ecs.systems;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Input extends GLFWKeyCallback {

    public static final int KEYS_ARRAY_SIZE = 0xffff;

    public static boolean[] pressedKeys     = new boolean[KEYS_ARRAY_SIZE];
    public static boolean[] releasedKeys    = new boolean[KEYS_ARRAY_SIZE];
    public static boolean[] holdenKeys      = new boolean[KEYS_ARRAY_SIZE];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        pressedKeys[key] = action == GLFW_PRESS;
        releasedKeys[key] = action == GLFW_RELEASE;
        holdenKeys[key] = action == GLFW_REPEAT;
    }

    public static boolean isHolden(int key) {
        return holdenKeys[key];
    }

    public static boolean isReleased(int key) {
        return releasedKeys[key];
    }

    public static boolean isPressed(int key) {
        return pressedKeys[key];
    }
}
