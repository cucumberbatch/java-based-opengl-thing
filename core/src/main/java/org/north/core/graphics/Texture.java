package org.north.core.graphics;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Texture implements Externalizable {

    private int texture;
    private int width, height;
    private boolean active;
    private String filePath;

    public Texture() {}

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
        this.filePath = path;
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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(filePath);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        this.filePath = in.readUTF();
        Texture textureInstance = TextureManager.tryToLoadTexture(filePath);
        this.width = textureInstance.width;
        this.height = textureInstance.height;
        this.texture = textureInstance.texture;
    }
}
