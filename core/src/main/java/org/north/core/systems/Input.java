package org.north.core.systems;

import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.*;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

public class Input {

    public static final int KEYS_ARRAY_SIZE = 0xffff;

    public static boolean[] pressedKeys     = new boolean[KEYS_ARRAY_SIZE];
    public static boolean[] releasedKeys    = new boolean[KEYS_ARRAY_SIZE];
    public static boolean[] holdenKeys      = new boolean[KEYS_ARRAY_SIZE];

    public static List<Integer> lastPressedKeys = new ArrayList<>();

    public static Vector2f  cursorPosition  = new Vector2f();


    public static class KeyboardInput extends GLFWKeyCallback {


        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            releasedKeys[key]   = action == GLFW_RELEASE;
            pressedKeys[key]    = action == GLFW_PRESS;
            holdenKeys[key]     = action == GLFW_REPEAT;

            if (action == GLFW_PRESS) {
                lastPressedKeys.add(key);
            }
        }
    }

    public static class MouseInput extends GLFWMouseButtonCallback {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            releasedKeys[button]   = action == GLFW_RELEASE;
            pressedKeys[button]    = action == GLFW_PRESS;
            holdenKeys[button]     = action == GLFW_REPEAT;

            if (action == GLFW_PRESS) {
                lastPressedKeys.add(button);
            }
        }
    }

    public static class CursorPositionInput extends GLFWCursorPosCallback {
        @Override
        public void invoke(long window, double xpos, double ypos) {
            cursorPosition.set((float) xpos, (float) ypos);
            // System.out.printf("%f\t%f\n", xpos, ypos);
        }
    }

    public static void updateInput() {
        for (Integer key : lastPressedKeys) {
            if (pressedKeys[key]) {
                pressedKeys[key]    = false;
                holdenKeys[key]     = true;
            }
        }
    }


    public static boolean isHeldDown(int key) {
        return holdenKeys[key];
    }

    public static boolean isReleased(int key) {
        return releasedKeys[key];
    }

    public static boolean isPressed(int key) {
        return pressedKeys[key];
    }

    public static Vector2f getCursorPosition() {
        return new Vector2f(cursorPosition);
    }
}
