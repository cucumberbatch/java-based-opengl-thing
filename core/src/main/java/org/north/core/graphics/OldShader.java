package org.north.core.graphics;

import org.north.core.utils.Logger;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.north.core.utils.ShaderUtils;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

public class OldShader {

    public static OldShader BACKGROUND;
    public static OldShader SIMPLE_COLOR_SHADER;
    public static OldShader TEXTURE_SHADER;

    private final int id;
    private final Map<String, Integer> uniformLocationCache = new HashMap<>();
    private boolean enabled;

    public OldShader(String vertex, String fragment) {
        id = ShaderUtils.load(vertex, fragment);
    }

    public static void loadAll() {
        BACKGROUND = new OldShader("core/src/main/resources/assets/shaders/bg.vert", "core/src/main/resources/assets/shaders/bg.frag");
        SIMPLE_COLOR_SHADER = new OldShader("core/src/main/resources/assets/shaders/simple_color_shader.vert", "core/src/main/resources/assets/shaders/simple_color_shader.frag");
        TEXTURE_SHADER = new OldShader("core/src/main/resources/assets/shaders/texture_shader.vert", "core/src/main/resources/assets/shaders/texture_shader.frag");
    }

    private int getUniformLocation(String name) {
        if (uniformLocationCache.containsKey(name)) {
            return uniformLocationCache.get(name);
        }
        int result = GL20.glGetUniformLocation(id, name);
        if (result == -1) {
            // Logger.error("Couldn't find uniform variable '" + name + "'!");
        } else {
            uniformLocationCache.put(name, result);
        }
        return result;
    }

    public void setUniform(String name, int value) {
        GL20.glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform(String name, float value) {
        GL20.glUniform1f(getUniformLocation(name), value);
    }

    public void setUniform(String name, int x, int y) {
        GL20.glUniform2i(getUniformLocation(name), x, y);
    }

    public void setUniform(String name, float x, float y) {
        GL20.glUniform2f(getUniformLocation(name), x, y);
    }

    public void setUniform(String name, Vector2f vector2) {
        GL20.glUniform2f(getUniformLocation(name), vector2.x, vector2.y);
    }

    public void setUniform(String name, int x, int y, int z) {
        GL20.glUniform3i(getUniformLocation(name), x, y, z);
    }

    public void setUniform(String name, float x, float y, float z) {
        GL20.glUniform3f(getUniformLocation(name), x, y, z);
    }

    public void setUniform(String name, Vector3f vector3) {
        GL20.glUniform3f(getUniformLocation(name), vector3.x, vector3.y, vector3.z);
    }

    public void setUniform(String name, int x, int y, int z, int w) {
        GL20.glUniform4i(getUniformLocation(name), x, y, z, w);
    }

    public void setUniform(String name, float x, float y, float z, float w) {
        GL20.glUniform4f(getUniformLocation(name), x, y, z, w);
    }

    public void setUniform(String name, Vector4f vector4) {
        GL20.glUniform4f(getUniformLocation(name), vector4.x, vector4.y, vector4.z, vector4.w);
    }

    public void setUniform(String name, Matrix4f matrix4f) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            matrix4f.get(fb);
            GL20.glUniformMatrix4fv(getUniformLocation(name), false, fb);
        }
    }

    public void enable() {
        GL20.glUseProgram(id);
        enabled = true;
    }

    public void disable() {
        GL20.glUseProgram(0);
        enabled = false;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getId() {
        return id;
    }

}
