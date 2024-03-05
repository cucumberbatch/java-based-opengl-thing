package org.north.core.graphics;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.north.core.component.MeshRenderer;
import org.north.core.exception.ShaderUniformNotFoundException;
import org.north.core.graphics.shader.Shader;
import org.north.core.reflection.di.Inject;
import org.north.core.utils.BufferUtils;

import java.util.HashMap;
import java.util.Map;

public class Graphics {
    public Window window;

    public Shader previouslyRenderedShader = null;
    public Texture  previouslyRenderedTexture = null;

    public Matrix4f projection = new Matrix4f().identity();
    public Matrix4f view = new Matrix4f().lookAt(new Vector3f(0, 0, -1), new Vector3f(0, 0, 1), new Vector3f(0, 1, 0));

    private final Map<String, Integer> uniformLocationCache = new HashMap<>();
    private int activeTextureCount;
    private final int[] activeTextureBuff = new int[4];

    @Inject
    public Graphics(Window window) {
        this.window = window;
    }


    public void prepareShader(Shader shader, MeshRenderer renderer) {
        activeTextureCount = GL20.GL_TEXTURE0;
        shader.updateUniforms(this, renderer);
    }

    public int getUniformLocation(Shader shader, String name) throws ShaderUniformNotFoundException {
        Integer pointer;
        if ((pointer = uniformLocationCache.get(name)) != null) {
            return pointer;
        }
        pointer = GL20.glGetUniformLocation(shader.getId(), name);
        if (pointer == -1) {
            throw new ShaderUniformNotFoundException(name, this.getClass().getSimpleName());
        } else {
            uniformLocationCache.put(name, pointer);
        }
        return pointer;
    }

    public void setUniform(Shader shader, String name, int value) throws ShaderUniformNotFoundException {
        GL20.glUniform1i(getUniformLocation(shader, name), value);
    }

    public void setUniform(Shader shader, String name, float value) throws ShaderUniformNotFoundException {
        GL20.glUniform1f(getUniformLocation(shader, name), value);
    }

    public void setUniform(Shader shader, String name, int x, int y) throws ShaderUniformNotFoundException {
        GL20.glUniform2i(getUniformLocation(shader, name), x, y);
    }

    public void setUniform(Shader shader, String name, float x, float y) throws ShaderUniformNotFoundException {
        GL20.glUniform2f(getUniformLocation(shader, name), x, y);
    }

    public void setUniform(Shader shader, String name, Vector2f vector2) throws ShaderUniformNotFoundException {
        GL20.glUniform2f(getUniformLocation(shader, name), vector2.x, vector2.y);
    }

    public void setUniform(Shader shader, String name, int x, int y, int z) throws ShaderUniformNotFoundException {
        GL20.glUniform3i(getUniformLocation(shader, name), x, y, z);
    }

    public void setUniform(Shader shader, String name, float x, float y, float z) throws ShaderUniformNotFoundException {
        GL20.glUniform3f(getUniformLocation(shader, name), x, y, z);
    }

    public void setUniform(Shader shader, String name, Vector3f vector3) throws ShaderUniformNotFoundException {
        GL20.glUniform3f(getUniformLocation(shader, name), vector3.x, vector3.y, vector3.z);
    }

    public void setUniform(Shader shader, String name, int x, int y, int z, int w) throws ShaderUniformNotFoundException {
        GL20.glUniform4i(getUniformLocation(shader, name), x, y, z, w);
    }

    public void setUniform(Shader shader, String name, float x, float y, float z, float w) throws ShaderUniformNotFoundException {
        GL20.glUniform4f(getUniformLocation(shader, name), x, y, z, w);
    }

    public void setUniform(Shader shader, String name, Vector4f vector4) throws ShaderUniformNotFoundException {
        GL20.glUniform4f(getUniformLocation(shader, name), vector4.x, vector4.y, vector4.z, vector4.w);
    }

    public void setUniform(Shader shader, String name, Texture texture) throws ShaderUniformNotFoundException {
        texture.bind();
        GL20.glActiveTexture(activeTextureCount);
        GL20.glGetIntegerv(GL20.GL_ACTIVE_TEXTURE, activeTextureBuff);
        GL20.glUniform1i(getUniformLocation(shader, name), activeTextureCount);
        // Logger.debug(String.format("Bounded texture with id: %s attached to slot: %s. Active textures buffer: %s",
//                texture.getId(), activeTextureCount, Arrays.toString(activeTextureBuff)));
        activeTextureCount++;
    }

    public void setUniform(Shader shader, String name, Matrix4f matrix4f) throws ShaderUniformNotFoundException {
        GL20.glUniformMatrix4fv(getUniformLocation(shader, name), false, BufferUtils.createFloatBuffer(matrix4f));
    }

    public void setUniform(int uniformPointer, int value) throws ShaderUniformNotFoundException {
        GL20.glUniform1i(uniformPointer, value);
    }

    public void setUniform(int uniformPointer, float value) throws ShaderUniformNotFoundException {
        GL20.glUniform1f(uniformPointer, value);
    }

    public void setUniform(int uniformPointer, int x, int y) throws ShaderUniformNotFoundException {
        GL20.glUniform2i(uniformPointer, x, y);
    }

    public void setUniform(int uniformPointer, float x, float y) throws ShaderUniformNotFoundException {
        GL20.glUniform2f(uniformPointer, x, y);
    }

    public void setUniform(int uniformPointer, Vector2f vector2) throws ShaderUniformNotFoundException {
        GL20.glUniform2f(uniformPointer, vector2.x, vector2.y);
    }

    public void setUniform(int uniformPointer, int x, int y, int z) throws ShaderUniformNotFoundException {
        GL20.glUniform3i(uniformPointer, x, y, z);
    }

    public void setUniform(int uniformPointer, float x, float y, float z) throws ShaderUniformNotFoundException {
        GL20.glUniform3f(uniformPointer, x, y, z);
    }

    public void setUniform(int uniformPointer, Vector3f vector3) throws ShaderUniformNotFoundException {
        GL20.glUniform3f(uniformPointer, vector3.x, vector3.y, vector3.z);
    }

    public void setUniform(int uniformPointer, int x, int y, int z, int w) throws ShaderUniformNotFoundException {
        GL20.glUniform4i(uniformPointer, x, y, z, w);
    }

    public void setUniform(int uniformPointer, float x, float y, float z, float w) throws ShaderUniformNotFoundException {
        GL20.glUniform4f(uniformPointer, x, y, z, w);
    }

    public void setUniform(int uniformPointer, Vector4f vector4) throws ShaderUniformNotFoundException {
        GL20.glUniform4f(uniformPointer, vector4.x, vector4.y, vector4.z, vector4.w);
    }

    public void setUniform(int uniformPointer, Texture texture) throws ShaderUniformNotFoundException {
        texture.bind();
        GL20.glActiveTexture(activeTextureCount);
        GL20.glGetIntegerv(GL20.GL_ACTIVE_TEXTURE, activeTextureBuff);
        GL20.glUniform1i(uniformPointer, activeTextureCount);
        // Logger.debug(String.format("Bounded texture with id: %s attached to slot: %s. Active textures buffer: %s",
//                texture.getId(), activeTextureCount, Arrays.toString(activeTextureBuff)));
        activeTextureCount++;
    }

    public void setUniform(int uniformPointer, Matrix4f matrix4f) throws ShaderUniformNotFoundException {
        GL20.glUniformMatrix4fv(uniformPointer, false, BufferUtils.createFloatBuffer(matrix4f));
    }

    private boolean isPreviouslyRenderedShader(Shader shader) {
        return previouslyRenderedShader != null && previouslyRenderedShader.getId() == shader.getId();
    }

    private boolean isPreviouslyRenderedTexture(Texture texture) {
        return previouslyRenderedTexture != null && previouslyRenderedTexture.getId() == texture.getId();
    }

}
