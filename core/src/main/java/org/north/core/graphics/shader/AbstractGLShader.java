package org.north.core.graphics.shader;

import org.lwjgl.opengl.GL20;
import org.north.core.utils.ResourceManager;

import java.io.*;

public abstract class AbstractGLShader implements Shader, Externalizable {

    protected int id;
    protected boolean enabled;

    private String vertexShaderPath;
    private String fragmentShaderPath;

    protected void load(String vertexShaderPath, String fragmentShaderPath) {
        this.vertexShaderPath = vertexShaderPath;
        this.fragmentShaderPath = fragmentShaderPath;
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
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(vertexShaderPath);
        out.writeUTF(fragmentShaderPath);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        String vertexShaderPath = in.readUTF();
        String fragmentShaderPath = in.readUTF();
        load(vertexShaderPath, fragmentShaderPath);
    }

}
