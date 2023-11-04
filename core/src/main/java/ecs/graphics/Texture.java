package ecs.graphics;

import ecs.utils.BufferUtils;
import ecs.utils.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class Texture {

    private int width, height;
    private int texture;

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
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getId() {
        return texture;
    }
}
