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

    public static final int KEYS_ARRAY_SIZE = 0xff;

//    public static boolean[] pressedKeys     = new boolean[KEYS_ARRAY_SIZE];
//    public static boolean[] releasedKeys    = new boolean[KEYS_ARRAY_SIZE];
//    public static boolean[] holdenKeys      = new boolean[KEYS_ARRAY_SIZE];

    public static BitSet pressedKeys     = new BitSet(KEYS_ARRAY_SIZE);
    public static BitSet releasedKeys    = new BitSet(KEYS_ARRAY_SIZE);
    public static BitSet holdenKeys      = new BitSet(KEYS_ARRAY_SIZE);

    public static List<Integer> lastPressedKeys = new ArrayList<>();

    public static Vector2f  cursorPosition  = new Vector2f();


    public static class KeyboardInput extends GLFWKeyCallback {


        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
//            releasedKeys[key]   = action == GLFW_RELEASE;
//            pressedKeys[key]    = action == GLFW_PRESS;
//            holdenKeys[key]     = action == GLFW_REPEAT;
//
            releasedKeys.set(key, action == GLFW_RELEASE);
            pressedKeys.set(key, action == GLFW_PRESS);
            holdenKeys.set(key, action == GLFW_REPEAT);

            if (action == GLFW_PRESS) {
                lastPressedKeys.add(key);
            }

        }
    }

    public static class MouseInput extends GLFWMouseButtonCallback {
        @Override
        public void invoke(long window, int button, int action, int mods) {
//            releasedKeys[button]   = action == GLFW_RELEASE;
//            pressedKeys[button]    = action == GLFW_PRESS;
//            holdenKeys[button]     = action == GLFW_REPEAT;

            releasedKeys.set(button, action == GLFW_RELEASE);
            pressedKeys.set(button, action == GLFW_PRESS);
            holdenKeys.set(button, action == GLFW_REPEAT);

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
//            if (pressedKeys[key]) {
//                pressedKeys[key]    = false;
//                holdenKeys[key]     = true;
//            }

            if (pressedKeys.get(key)) {
                pressedKeys.set(key, false);
                holdenKeys.set(key, true);
            }
        }
        lastPressedKeys.clear();
    }


    public static boolean isHeldDown(int key) {
//        return holdenKeys[key];
        return holdenKeys.get(key);
    }

    public static boolean isReleased(int key) {
//        return releasedKeys[key];
        return releasedKeys.get(key);
    }

    public static boolean isPressed(int key) {
//        return pressedKeys[key];
        return pressedKeys.get(key);
    }

    public static Vector2f getCursorPosition() {
        return new Vector2f(cursorPosition);
    }

    public static Vector2f getCursorPosition(Vector2f destination) {
        return destination.set(cursorPosition);
    }

    public static float getCursorX() {
        return cursorPosition.x;
    }

    public static float getCursorY() {
        return cursorPosition.y;
    }
}
