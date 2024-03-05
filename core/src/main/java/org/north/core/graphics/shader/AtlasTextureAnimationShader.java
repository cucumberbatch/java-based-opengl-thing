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

    private boolean initializedPtr;
    private int spriteIndexPtr;
    private int spriteWidthPtr;
    private int texturePtr;
    private int colorPtr;
    private int projectionPtr;
    private int viewPtr;
    private int modelPtr;

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
    public void updateUniforms(Graphics graphics, MeshRenderer renderer) {
        if (!initializedPtr) {
            spriteIndexPtr = graphics.getUniformLocation(this, "u_sprite_index");
            spriteWidthPtr = graphics.getUniformLocation(this, "u_sprite_width");
            texturePtr = graphics.getUniformLocation(this, "u_texture");
            colorPtr = graphics.getUniformLocation(this, "u_color");
            projectionPtr = graphics.getUniformLocation(this, "u_projection");
            viewPtr = graphics.getUniformLocation(this, "u_view");
            modelPtr = graphics.getUniformLocation(this, "u_model");
            initializedPtr = true;
        }

        graphics.setUniform(spriteIndexPtr, spriteIndex);
        graphics.setUniform(spriteWidthPtr, spriteWidth);
        graphics.setUniform(texturePtr, renderer.texture);
        graphics.setUniform(colorPtr, renderer.color);
        graphics.setUniform(projectionPtr, graphics.projection);
        graphics.setUniform(viewPtr, graphics.view);
        graphics.setUniform(modelPtr, renderer.getTransform().getGlobalModelMatrix(temp));
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
