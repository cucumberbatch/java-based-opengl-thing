package org.north.core.graphics.shader;

import org.joml.Matrix4f;
import org.north.core.component.MeshRenderer;
import org.north.core.graphics.Graphics;

import java.io.*;

public class AtlasTextureAnimationShader extends AbstractGLShader {

    public int spriteCount;
    public int spriteWidth;
    public int spriteHeight;

    public int spriteIndex;

    private final Matrix4f temp = new Matrix4f();

    public AtlasTextureAnimationShader() {
    }

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
        setUniform("u_model", renderer.getTransform().getGlobalModelMatrix(temp));
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeInt(spriteCount);
        out.writeInt(spriteWidth);
        out.writeInt(spriteHeight);
        out.writeInt(spriteIndex);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        super.readExternal(in);
        spriteCount = in.readInt();
        spriteWidth = in.readInt();
        spriteHeight = in.readInt();
        spriteIndex = in.readInt();
    }
}
