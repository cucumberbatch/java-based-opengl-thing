package org.north.core.graphics.shader;

import org.joml.Matrix4f;
import org.north.core.components.MeshRenderer;
import org.north.core.graphics.Graphics;

public class AtlasTextureAnimationShader extends AbstractGLShader {

    public final int spriteCount;
    public final int spriteWidth;
    public final int spriteHeight;

    public int spriteIndex;

    private final Matrix4f temp = new Matrix4f();

    public AtlasTextureAnimationShader(int spriteCount, int spriteWidth, int spriteHeight) {
        String vertexShaderPath = "atlas_texture_animation_shader.vert";
        String fragmentShaderPath = "atlas_texture_animation_shader.frag";
        load(vertexShaderPath, fragmentShaderPath);
        this.spriteCount = spriteCount;
        this.spriteWidth = spriteWidth;
        this.spriteHeight = spriteHeight;
    }

    public void incrementSpriteIndex() {
        spriteIndex = Math.max(1, (spriteIndex + 1) % (spriteCount));
    }

    @Override
    protected void updateUniforms(Graphics graphics, MeshRenderer renderer) {
        setUniform("u_sprite_index", spriteIndex);
        setUniform("u_sprite_width", spriteWidth);
        setUniform("u_texture", renderer.texture);
        setUniform("u_color", renderer.color);
        setUniform("u_projection", graphics.projection);
        setUniform("u_view", graphics.view);
        setUniform("u_model", renderer.getTransform().getWorldModelMatrix(temp));
    }
}
