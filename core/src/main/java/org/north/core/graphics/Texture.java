package org.north.core.graphics;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Texture {

    private final int texture;
    private final int width, height;
    private boolean active;

    public Texture(int width, int height, int texture) {
        this.width = width;
        this.height = height;
        this.texture = texture;
    }

    public Texture(String path) {
        Texture textureInstance = TextureManager.tryToLoadTexture(path);
        this.width = textureInstance.width;
        this.height = textureInstance.height;
        this.texture = textureInstance.texture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, texture);
        active = true;
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public int getId() {
        return texture;
    }
}
