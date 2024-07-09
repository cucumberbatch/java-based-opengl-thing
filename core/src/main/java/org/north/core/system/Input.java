package org.north.core.system;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    public static final int KEYS_ARRAY_SIZE = 0xff;

    private static Input instance;

    public BitSet pressedKeys = new BitSet(KEYS_ARRAY_SIZE);
    public BitSet releasedKeys = new BitSet(KEYS_ARRAY_SIZE);
    public BitSet holdedKeys = new BitSet(KEYS_ARRAY_SIZE);

    public List<Integer> lastPressedKeys = new ArrayList<>();

    public Vector2f cursorPosition = new Vector2f();

    public Input() {
        Input.instance = this;
    }

    public static synchronized Input getInstance() {
        if (Input.instance == null) {
            Input.instance = new Input();
        }
        return Input.instance;
    }

    public void updateInput() {
        for (Integer key : lastPressedKeys) {
            if (pressedKeys.get(key)) {
                pressedKeys.set(key, false);
                holdedKeys.set(key, true);
            }
        }
        lastPressedKeys.clear();
    }

    public boolean isHolded(int key) {
        return holdedKeys.get(key);
    }

    public boolean isReleased(int key) {
        return releasedKeys.get(key);
    }

    public boolean isPressed(int key) {
        return pressedKeys.get(key);
    }

    public Vector2f getCursorPosition() {
        return new Vector2f(cursorPosition);
    }

    public Vector2f getCursorPosition(Vector2f destination) {
        return destination.set(cursorPosition);
    }

    public float getCursorX() {
        return cursorPosition.x;
    }

    public float getCursorY() {
        return cursorPosition.y;
    }

    public static class KeyboardInput extends GLFWKeyCallback {
        private final Input input = Input.getInstance();

        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            if (key < 0) return;

            input.releasedKeys.set(key, action == GLFW_RELEASE);
            input.pressedKeys.set(key, action == GLFW_PRESS);
            input.holdedKeys.set(key, action == GLFW_REPEAT);

            if (action == GLFW_PRESS) {
                input.lastPressedKeys.add(key);
            }

        }
    }

    public static class MouseInput extends GLFWMouseButtonCallback {
        private final Input input = Input.getInstance();

        @Override
        public void invoke(long window, int button, int action, int mods) {
            input.releasedKeys.set(button, action == GLFW_RELEASE);
            input.pressedKeys.set(button, action == GLFW_PRESS);
            input.holdedKeys.set(button, action == GLFW_REPEAT);

            if (action == GLFW_PRESS) {
                input.lastPressedKeys.add(button);
            }
        }
    }

    public static class CursorPositionInput extends GLFWCursorPosCallback {
        private final Input input = Input.getInstance();

        @Override
        public void invoke(long window, double xpos, double ypos) {
            input.cursorPosition.set((float) xpos, (float) ypos);
        }
    }
}
