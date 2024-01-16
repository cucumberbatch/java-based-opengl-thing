package org.north.core.graphics;

import org.north.core.components.MeshRenderer;

public class AtlasTextureAnimationShader extends AbstractGLShader {

    public final int spriteCount;
    public final int spriteWidth;
    public final int spriteHeight;

    public int spriteIndex;

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
        setUniform("u_model", renderer.getTransform().getLocalModelMatrix());
    }
}
