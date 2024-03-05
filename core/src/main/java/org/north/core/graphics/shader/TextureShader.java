package org.north.core.graphics.shader;

import org.joml.Matrix4f;
import org.north.core.component.MeshRenderer;
import org.north.core.graphics.Graphics;

public class TextureShader extends AbstractGLShader {

    private final Matrix4f temp = new Matrix4f();

    private int textureUniformPtr;
    private int colorUniformPtr;
    private int projectionUniformPtr;
    private int viewUniformPtr;
    private int modelUniformPtr;

    private boolean initializedPtr = false;

    public TextureShader() {
        String vertexShaderPath = "texture_shader.vert";
        String fragmentShaderPath = "texture_shader.frag";
        load(vertexShaderPath, fragmentShaderPath);
    }

    @Override
    public void updateUniforms(Graphics graphics, MeshRenderer renderer) {
        if (!initializedPtr) {
            textureUniformPtr = graphics.getUniformLocation(this, "u_texture");
            colorUniformPtr = graphics.getUniformLocation(this, "u_color");
            projectionUniformPtr = graphics.getUniformLocation(this, "u_projection");
            viewUniformPtr = graphics.getUniformLocation(this, "u_view");
            modelUniformPtr = graphics.getUniformLocation(this, "u_model");
            initializedPtr = true;
        }

        graphics.setUniform(textureUniformPtr, renderer.texture);
        graphics.setUniform(colorUniformPtr, renderer.color);
        graphics.setUniform(projectionUniformPtr, graphics.projection);
        graphics.setUniform(viewUniformPtr, graphics.view);
        graphics.setUniform(modelUniformPtr, renderer.getTransform().getGlobalModelMatrix(temp));
    }
}
