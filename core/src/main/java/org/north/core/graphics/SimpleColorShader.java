package org.north.core.graphics;

import org.joml.Matrix4f;
import org.north.core.components.MeshRenderer;

public class SimpleColorShader extends AbstractGLShader {

    private final Matrix4f temp = new Matrix4f();

    public SimpleColorShader() {
        String vertexShaderPath = "simple_color_shader.vert";
        String fragmentShaderPath = "simple_color_shader.frag";
        load(vertexShaderPath, fragmentShaderPath);
    }

    @Override
    public void updateUniforms(Graphics graphics, MeshRenderer renderer) {
        setUniform("u_color", renderer.color);
        setUniform("u_projection", graphics.projection);
        setUniform("u_view", graphics.view);
        setUniform("u_model", renderer.getTransform().getLocalModelMatrix(temp));
    }
}
