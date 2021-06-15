package ecs.systems;

import ecs.math.Vector2f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

public class Input {

    public static final int KEYS_ARRAY_SIZE = 0xffff;

    public static boolean[] pressedKeys     = new boolean[KEYS_ARRAY_SIZE];
    public static boolean[] releasedKeys    = new boolean[KEYS_ARRAY_SIZE];
    public static boolean[] holdenKeys      = new boolean[KEYS_ARRAY_SIZE];

    public static Vector2f  cursorPosition  = new Vector2f();


    public static class KeyboardInput extends GLFWKeyCallback {

        private static List<Integer> lastPressedKeys = new ArrayList<>();

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

    public static class CursorPositionInput extends GLFWCursorPosCallback {
        @Override
        public void invoke(long window, double xpos, double ypos) {
            cursorPosition.set((float) xpos, (float) ypos);
        }
    }

    public static void updateInput() {
        for (Integer key : KeyboardInput.lastPressedKeys) {
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
        return cursorPosition;
    }
}
