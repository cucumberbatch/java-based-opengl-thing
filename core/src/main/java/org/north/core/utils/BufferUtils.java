package org.north.core.utils;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.*;
import java.util.HashMap;
import java.util.Map;

public class BufferUtils {

    private static final Map<Integer, ByteBuffer> BYTE_BUFFER_STORAGE = new HashMap<>();
    private static final Map<Integer, IntBuffer> INT_BUFFER_STORAGE = new HashMap<>();
    private static final Map<Integer, FloatBuffer> FLOAT_BUFFER_STORAGE = new HashMap<>();

    private static final float[] M4F_BUFFER_ARRAY = new float[16];

    private static final boolean newMemoryAllocation = true;

    public static ByteBuffer createByteBuffer(byte[] array) {
        ByteBuffer buffer;
        if (BYTE_BUFFER_STORAGE.get(array.length) != null) {
            buffer = BYTE_BUFFER_STORAGE.get(array.length);
            buffer.clear().put(array).flip();
            return buffer;
        }

        if (newMemoryAllocation) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                buffer = stack.malloc(array.length);
            }
        } else {
            buffer = ByteBuffer.allocateDirect(array.length);
        }

        // Logger.debug(String.format("ByteBuffer of size %d created", buffer.capacity()));
        buffer.order(ByteOrder.nativeOrder()).put(array).flip();
        BYTE_BUFFER_STORAGE.put(array.length, buffer);
        return buffer;
    }

    public static IntBuffer createIntBuffer(int[] array) {
        IntBuffer buffer;
        if (INT_BUFFER_STORAGE.get(array.length) != null) {
            buffer = INT_BUFFER_STORAGE.get(array.length);
            buffer.clear().put(array).flip();
            return buffer;
        }
        buffer = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asIntBuffer();
        Logger.debug(String.format("IntBuffer of size %d created", buffer.capacity()));
        buffer.put(array).flip();
        INT_BUFFER_STORAGE.put(array.length, buffer);
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(float[] array) {
        FloatBuffer buffer;
        if (FLOAT_BUFFER_STORAGE.get(array.length) != null) {
            buffer = FLOAT_BUFFER_STORAGE.get(array.length);
            buffer.clear().put(array).flip();
            return buffer;
        }
        buffer = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        // Logger.debug(String.format("FloatBuffer of size %d created", buffer.capacity()));
        buffer.put(array).flip();
        FLOAT_BUFFER_STORAGE.put(array.length, buffer);
        return buffer;
    }

    public static FloatBuffer createFloatBuffer(Matrix4f matrix4f) {
        FloatBuffer buffer;
        if (FLOAT_BUFFER_STORAGE.get(16) != null) {
            buffer = FLOAT_BUFFER_STORAGE.get(16);
            return matrix4f.get(buffer);
        }

        if (newMemoryAllocation) {
            try (MemoryStack stack = MemoryStack.stackPush()) {
                buffer = stack.mallocFloat(16);
            }
        } else {
            buffer = ByteBuffer.allocateDirect(16 << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        }

//        buffer = ByteBuffer.allocateDirect(16 << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
        // Logger.debug(String.format("FloatBuffer of size %d created", buffer.capacity()));
        matrix4f.get(buffer);
        FLOAT_BUFFER_STORAGE.put(16, buffer);
        return buffer;
    }
}
