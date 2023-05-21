package ecs.graphics;

import ecs.utils.BufferUtils;
import matrices.Matrix4f;
import org.lwjgl.opengl.GL20;
import vectors.Vector2f;
import vectors.Vector3f;
import vectors.Vector4f;
import ecs.utils.ShaderUtils;

import java.util.HashMap;
import java.util.Map;

public class Shader {

    public static final int VERTEX_ATTRIBUTE = 0;
    public static final int TEXTURE_COORDINATE_ATTRIBUTE = 1;

    public static Shader BACKGROUND;
    public static Shader GUI;
    public static Shader TEST;

    private final int id;
    private Map<String, Integer> uniformLocationCache = new HashMap<>();
    private boolean enabled;

    public Shader(String vertex, String fragment) {
        id = ShaderUtils.load(vertex, fragment);
    }

    public static void loadAll() {
        BACKGROUND = new Shader("core/assets/shaders/bg.vert",   "core/assets/shaders/bg.frag");
        GUI        = new Shader("core/assets/shaders/simple_color_shader.vert",  "core/assets/shaders/simple_color_shader.frag");
        TEST       = new Shader("core/assets/shaders/texture_shader.vert", "core/assets/shaders/texture_shader.frag");
    }

    public int getUniformLocation(String name) {
        if (uniformLocationCache.containsKey(name)) {
            return uniformLocationCache.get(name);
        }
        int result = GL20.glGetUniformLocation(id, name);
        if (result == -1) {
            System.err.println("Couldn't find uniform variable '" + name + "'!");
        } else {
            uniformLocationCache.put(name, result);
        }
        return result;
    }

    public void setUniform(String name, int value) {
        if (!enabled) enable();
        GL20.glUniform1i(getUniformLocation(name), value);
    }

    public void setUniform(String name, float value) {
        if (!enabled) enable();
        GL20.glUniform1f(getUniformLocation(name), value);
    }

    public void setUniform(String name, int x, int y) {
        if (!enabled) enable();
        GL20.glUniform2i(getUniformLocation(name), x, y);
    }

    public void setUniform(String name, float x, float y) {
        if (!enabled) enable();
        GL20.glUniform2f(getUniformLocation(name), x, y);
    }

    public void setUniform(String name, Vector2f vector2) {
        if (!enabled) enable();
        GL20.glUniform2f(getUniformLocation(name), vector2.x, vector2.y);
    }

    public void setUniform(String name, int x, int y, int z) {
        if (!enabled) enable();
        GL20.glUniform3i(getUniformLocation(name), x, y, z);
    }

    public void setUniform(String name, float x, float y, float z) {
        if (!enabled) enable();
        GL20.glUniform3f(getUniformLocation(name), x, y, z);
    }

    public void setUniform(String name, Vector3f vector3) {
        if (!enabled) enable();
        GL20.glUniform3f(getUniformLocation(name), vector3.x, vector3.y, vector3.z);
    }

    public void setUniform(String name, int x, int y, int z, int w) {
        if (!enabled) enable();
        GL20.glUniform4i(getUniformLocation(name), x, y, z, w);
    }

    public void setUniform(String name, float x, float y, float z, float w) {
        if (!enabled) enable();
        GL20.glUniform4f(getUniformLocation(name), x, y, z, w);
    }

    public void setUniform(String name, Vector4f vector4) {
        if (!enabled) enable();
        GL20.glUniform4f(getUniformLocation(name), vector4.x, vector4.y, vector4.z, vector4.w);
    }

    public void setUniformMat4f(String name, Matrix4f matrix4f) {
        if (!enabled) enable();
        GL20.glUniformMatrix4fv(getUniformLocation(name), false, BufferUtils.createFloatBuffer(matrix4f.elements));
    }

    public void enable() {
        GL20.glUseProgram(id);
        enabled = true;
    }

    public void disable() {
        GL20.glUseProgram(0);
        enabled = false;
    }

    public int getId() {
        return id;
    }

}
