package org.north.core.graphics.shader;

import org.north.core.component.MeshRenderer;
import org.north.core.component.Transform;
import org.north.core.graphics.Graphics;

public class PBRShader extends AbstractGLShader {

    protected PBRShader() {
        String vertexShaderPath = "";
        String fragmentShaderPath = "";
        load(vertexShaderPath, fragmentShaderPath);
    }

    @Override
    public void updateUniforms(Graphics graphics, Transform transform, MeshRenderer renderer) {
        graphics.setUniform(this, "u_camera_position",   transform.getPosition());
        graphics.setUniform(this, "u_albedo",            renderer.color);
        graphics.setUniform(this, "u_metallic",          renderer.color);
        graphics.setUniform(this, "u_roughness",         renderer.color);
        graphics.setUniform(this, "u_ambient_occlusion", renderer.color);
    }
}
