package org.north.core.graphics.shader;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.north.core.components.MeshRenderer;
import org.north.core.exception.ShaderUniformNotFoundException;
import org.north.core.graphics.Graphics;
import org.north.core.graphics.Texture;
import org.north.core.utils.BufferUtils;
import org.north.core.utils.ResourceManager;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractGLShader implements Shader {

    protected int id;
    protected Map<String, Integer> uniformLocationCache = new HashMap<>();
    protected int activeTextureCount;
    protected int[] activeTextureBuff = new int[4];

    protected boolean enabled;

    protected void load(String vertexShaderPath, String fragmentShaderPath) {
        ResourceManager resourceManager = ResourceManager.getInstance();
        id = resourceManager.loadShader(vertexShaderPath, fragmentShaderPath);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void enable() {
        GL20.glUseProgram(id);
        enabled = true;
    }

    @Override
    public void disable() {
        GL20.glUseProgram(0);
        enabled = false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void prepareShader(Graphics graphics, MeshRenderer renderer) {
        activeTextureCount = GL20.GL_TEXTURE0;
        updateUniforms(graphics, renderer);
    }

    protected abstract void updateUniforms(Graphics graphics, MeshRenderer renderer);

    protected int getUniformLocation(String name) throws ShaderUniformNotFoundException {
        Integer pointer;
        if ((pointer = uniformLocationCache.get(name)) != null) {
            return pointer;
        }
        pointer = GL20.glGetUniformLocation(id, name);
        if (pointer == -1) {
            throw new ShaderUniformNotFoundException(name, this.getClass().getSimpleName());
        } else {
            uniformLocationCache.put(name, pointer);
        }
        return pointer;
    }

    protected void setUniform(String name, int value) throws ShaderUniformNotFoundException {
        GL20.glUniform1i(getUniformLocation(name), value);
    }

    protected void setUniform(String name, float value) throws ShaderUniformNotFoundException {
        GL20.glUniform1f(getUniformLocation(name), value);
    }

    protected void setUniform(String name, int x, int y) throws ShaderUniformNotFoundException {
        GL20.glUniform2i(getUniformLocation(name), x, y);
    }

    protected void setUniform(String name, float x, float y) throws ShaderUniformNotFoundException {
        GL20.glUniform2f(getUniformLocation(name), x, y);
    }

    protected void setUniform(String name, Vector2f vector2) throws ShaderUniformNotFoundException {
        GL20.glUniform2f(getUniformLocation(name), vector2.x, vector2.y);
    }

    protected void setUniform(String name, int x, int y, int z) throws ShaderUniformNotFoundException {
        GL20.glUniform3i(getUniformLocation(name), x, y, z);
    }

    protected void setUniform(String name, float x, float y, float z) throws ShaderUniformNotFoundException {
        GL20.glUniform3f(getUniformLocation(name), x, y, z);
    }

    protected void setUniform(String name, Vector3f vector3) throws ShaderUniformNotFoundException {
        GL20.glUniform3f(getUniformLocation(name), vector3.x, vector3.y, vector3.z);
    }

    protected void setUniform(String name, int x, int y, int z, int w) throws ShaderUniformNotFoundException {
        GL20.glUniform4i(getUniformLocation(name), x, y, z, w);
    }

    protected void setUniform(String name, float x, float y, float z, float w) throws ShaderUniformNotFoundException {
        GL20.glUniform4f(getUniformLocation(name), x, y, z, w);
    }

    protected void setUniform(String name, Vector4f vector4) throws ShaderUniformNotFoundException {
        GL20.glUniform4f(getUniformLocation(name), vector4.x, vector4.y, vector4.z, vector4.w);
    }

    protected void setUniform(String name, Texture texture) throws ShaderUniformNotFoundException {
        texture.bind();
        GL20.glActiveTexture(activeTextureCount);
        GL20.glGetIntegerv(GL20.GL_ACTIVE_TEXTURE, activeTextureBuff);
        GL20.glUniform1i(getUniformLocation(name), activeTextureCount);
        // Logger.debug(String.format("Bounded texture with id: %s attached to slot: %s. Active textures buffer: %s",
//                texture.getId(), activeTextureCount, Arrays.toString(activeTextureBuff)));
        activeTextureCount++;
    }

    protected void setUniform(String name, Matrix4f matrix4f) throws ShaderUniformNotFoundException {
        GL20.glUniformMatrix4fv(getUniformLocation(name), false, BufferUtils.createFloatBuffer(matrix4f));
    }

    protected void setUniform(int uniformPointer, int value) throws ShaderUniformNotFoundException {
        GL20.glUniform1i(uniformPointer, value);
    }

    protected void setUniform(int uniformPointer, float value) throws ShaderUniformNotFoundException {
        GL20.glUniform1f(uniformPointer, value);
    }

    protected void setUniform(int uniformPointer, int x, int y) throws ShaderUniformNotFoundException {
        GL20.glUniform2i(uniformPointer, x, y);
    }

    protected void setUniform(int uniformPointer, float x, float y) throws ShaderUniformNotFoundException {
        GL20.glUniform2f(uniformPointer, x, y);
    }

    protected void setUniform(int uniformPointer, Vector2f vector2) throws ShaderUniformNotFoundException {
        GL20.glUniform2f(uniformPointer, vector2.x, vector2.y);
    }

    protected void setUniform(int uniformPointer, int x, int y, int z) throws ShaderUniformNotFoundException {
        GL20.glUniform3i(uniformPointer, x, y, z);
    }

    protected void setUniform(int uniformPointer, float x, float y, float z) throws ShaderUniformNotFoundException {
        GL20.glUniform3f(uniformPointer, x, y, z);
    }

    protected void setUniform(int uniformPointer, Vector3f vector3) throws ShaderUniformNotFoundException {
        GL20.glUniform3f(uniformPointer, vector3.x, vector3.y, vector3.z);
    }

    protected void setUniform(int uniformPointer, int x, int y, int z, int w) throws ShaderUniformNotFoundException {
        GL20.glUniform4i(uniformPointer, x, y, z, w);
    }

    protected void setUniform(int uniformPointer, float x, float y, float z, float w) throws ShaderUniformNotFoundException {
        GL20.glUniform4f(uniformPointer, x, y, z, w);
    }

    protected void setUniform(int uniformPointer, Vector4f vector4) throws ShaderUniformNotFoundException {
        GL20.glUniform4f(uniformPointer, vector4.x, vector4.y, vector4.z, vector4.w);
    }

    protected void setUniform(int uniformPointer, Texture texture) throws ShaderUniformNotFoundException {
        texture.bind();
        GL20.glActiveTexture(activeTextureCount);
        GL20.glGetIntegerv(GL20.GL_ACTIVE_TEXTURE, activeTextureBuff);
        GL20.glUniform1i(uniformPointer, activeTextureCount);
        // Logger.debug(String.format("Bounded texture with id: %s attached to slot: %s. Active textures buffer: %s",
//                texture.getId(), activeTextureCount, Arrays.toString(activeTextureBuff)));
        activeTextureCount++;
    }

    protected void setUniform(int uniformPointer, Matrix4f matrix4f) throws ShaderUniformNotFoundException {
        GL20.glUniformMatrix4fv(uniformPointer, false, BufferUtils.createFloatBuffer(matrix4f));
    }

}
