package ecs.graphics;

import ecs.math.Matrix4f;
import ecs.math.Vector3f;
import ecs.utils.ShaderUtils;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    public static final int VERTEX_ATTRIBUTE = 0;
    public static final int TEXTURE_COORDINATE_ATTRIBUTE = 1;

    public static Shader BACKGROUND;

    private final int ID;
    private Map<String, Integer> locationCache = new HashMap<>();
    private boolean enabled;

    public Shader(String vertex, String fragment) {
        ID = ShaderUtils.load(vertex, fragment);
    }

    public static void loadAll() {
        BACKGROUND = new Shader("/shaders/bg.vert", "/shaders/bg.frag");
    }

    public int getUniform(String name) {
        if (locationCache.containsKey(name)) {
            return locationCache.get(name);
        }
        int result = glGetUniformLocation(ID, name);
        if (result == -1) {
            System.err.println("Couldn't find uniform variable '" + name + "'!");
        } else {
            locationCache.put(name, result);
        }
        return result;
    }

    public void setUniform1i(String name, int value) {
        if (!enabled) enable();
        glUniform1i(getUniform(name), value);
    }

    public void setUniform1f(String name, float value) {
        if (!enabled) enable();
        glUniform1f(getUniform(name), value);
    }

    public void setUniform2f(String name, float x, float y) {
        if (!enabled) enable();
        glUniform2f(getUniform(name), x, y);
    }

    public void setUniform3f(String name, Vector3f vector3) {
        if (!enabled) enable();
        glUniform3f(getUniform(name), vector3.x, vector3.y, vector3.z);
    }

    public void setUniformMat4f(String name, Matrix4f matrix4f) {
        if (!enabled) enable();
        glUniformMatrix4fv(getUniform(name), false, matrix4f.toFloatBuffer());
    }

    public void enable() {
        glUseProgram(ID);
        enabled = true;
    }

    public void disable() {
        glUseProgram(0);
        enabled = false;
    }

}
