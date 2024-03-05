package org.north.core.graphics.shader;

import org.north.core.component.MeshRenderer;
import org.north.core.graphics.Graphics;

public class PBRShader extends AbstractGLShader {

    protected PBRShader() {
        String vertexShaderPath = "";
        String fragmentShaderPath = "";
        load(vertexShaderPath, fragmentShaderPath);
    }

    @Override
    public void updateUniforms(Graphics graphics, MeshRenderer renderer) {
        graphics.setUniform(this, "u_camera_position", renderer.getTransform().position);
        graphics.setUniform(this, "u_albedo", renderer.color);
        graphics.setUniform(this, "u_metallic", renderer.color);
        graphics.setUniform(this, "u_roughness", renderer.color);
        graphics.setUniform(this, "u_ambient_occlusion", renderer.color);
    }
}
