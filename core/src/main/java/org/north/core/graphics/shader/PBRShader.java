package org.north.core.graphics.shader;

import org.north.core.components.MeshRenderer;
import org.north.core.graphics.Graphics;

public class PBRShader extends AbstractGLShader {

    protected PBRShader() {
        String vertexShaderPath = "";
        String fragmentShaderPath = "";
        load(vertexShaderPath, fragmentShaderPath);
    }

    @Override
    public void updateUniforms(Graphics graphics, MeshRenderer renderer) {
        setUniform("u_camera_position", renderer.getTransform().position);
        setUniform("u_albedo", renderer.color);
        setUniform("u_metallic", renderer.color);
        setUniform("u_roughness", renderer.color);
        setUniform("u_ambient_occlusion", renderer.color);
    }
}