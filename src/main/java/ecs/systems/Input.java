package ecs.systems;

import org.lwjgl.glfw.GLFWKeyCallback;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Input extends GLFWKeyCallback {

    public static boolean[] keys = new boolean[0xffff];

    @Override
    public void invoke(long window, int key, int scancode, int action, int mods) {
        keys[key] = action != GLFW_RELEASE;
    }

    public static boolean isPressed(int key) {
        return keys[key];
    }
}
