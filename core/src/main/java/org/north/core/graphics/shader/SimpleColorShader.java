package org.north.core.graphics.shader;

import org.joml.Matrix4f;
import org.north.core.components.MeshRenderer;
import org.north.core.graphics.Graphics;

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
    public void updateUniforms(Graphics graphics, MeshRenderer renderer) {
        if (!initializedPtr) {
            colorUniformPtr = getUniformLocation("u_color");
            projectionUniformPtr = getUniformLocation("u_projection");
            viewUniformPtr = getUniformLocation("u_view");
            modelUniformPtr = getUniformLocation("u_model");
            initializedPtr = true;
        }

        setUniform(colorUniformPtr, renderer.color);
        setUniform(projectionUniformPtr, graphics.projection);
        setUniform(viewUniformPtr, graphics.view);
        setUniform(modelUniformPtr, renderer.getTransform().getLocalModelMatrix(temp));
    }
}
