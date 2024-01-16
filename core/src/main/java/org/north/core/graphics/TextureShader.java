package org.north.core.graphics;

import org.north.core.components.MeshRenderer;

public class TextureShader extends AbstractGLShader {

    public TextureShader() {
        String vertexShaderPath = "texture_shader.vert";
        String fragmentShaderPath = "texture_shader.frag";
        load(vertexShaderPath, fragmentShaderPath);
    }

    @Override
    public void updateUniforms(Graphics graphics, MeshRenderer renderer) {
        setUniform("u_texture", renderer.texture);
        setUniform("u_color", renderer.color);
        setUniform("u_projection", graphics.projection);
        setUniform("u_view", graphics.view);
        setUniform("u_model", renderer.getTransform().getLocalModelMatrix());
    }
}
