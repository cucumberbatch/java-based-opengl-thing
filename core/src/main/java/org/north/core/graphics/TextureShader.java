package org.north.core.graphics;

import org.north.core.components.MeshRenderer;

public class TextureShader extends AbstractGLShader {

    private float acc;

    public TextureShader() {
        String vertexShaderPath = "texture_shader.vert";
        String fragmentShaderPath = "texture_shader.frag";
        load(vertexShaderPath, fragmentShaderPath);
        acc = 0;
    }

    @Override
    public void updateUniforms(Graphics graphics, MeshRenderer renderer) {
        setUniform("u_texture", renderer.texture);
        setUniform("u_texture2", renderer.texture2);
        setUniform("u_color", renderer.color);
        setUniform("u_projection", graphics.projection);
        setUniform("u_view", graphics.view);
        setUniform("u_model", renderer.transform.getLocalModelMatrix());
        setUniform("u_ratio", ((float) Math.sin(acc) + 1) / 2);
        acc = acc + 0.05f;
    }
}
