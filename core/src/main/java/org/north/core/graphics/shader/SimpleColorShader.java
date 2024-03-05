package org.north.core.graphics.shader;

import org.joml.Matrix4f;
import org.north.core.component.MeshRenderer;
import org.north.core.graphics.Graphics;

import java.io.*;

public class SimpleColorShader extends AbstractGLShader {

    private final Matrix4f temp = new Matrix4f();

    private int colorUniformPtr;
    private int projectionUniformPtr;
    private int viewUniformPtr;
    private int modelUniformPtr;

    private boolean initializedPtr = false;

    public SimpleColorShader() {
        String vertexShaderPath = "simple_color_shader.vert";
        String fragmentShaderPath = "simple_color_shader.frag";
        load(vertexShaderPath, fragmentShaderPath);
    }

    @Override
    public void updateUniforms(Graphics g, MeshRenderer renderer) {
        if (!initializedPtr) {
            colorUniformPtr = g.getUniformLocation(this, "u_color");
            projectionUniformPtr = g.getUniformLocation(this, "u_projection");
            viewUniformPtr = g.getUniformLocation(this, "u_view");
            modelUniformPtr = g.getUniformLocation(this, "u_model");
            initializedPtr = true;
        }

        g.setUniform(colorUniformPtr, renderer.color);
        g.setUniform(projectionUniformPtr, g.projection);
        g.setUniform(viewUniformPtr, g.view);
        g.setUniform(modelUniformPtr, renderer.getTransform().getGlobalModelMatrix(temp));
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        super.readExternal(in);
    }
}
