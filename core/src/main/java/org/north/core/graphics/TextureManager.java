package org.north.core.graphics;

import org.north.core.utils.BufferUtils;
import org.north.core.utils.Logger;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class TextureManager {
    private static Map<String, Texture> preloadedTexturesMap = new HashMap<>();

    public static Texture tryToLoadTexture(String textureFilePath) {
        if (preloadedTexturesMap.containsKey(textureFilePath)) {
            return preloadedTexturesMap.get(textureFilePath);
        }
        Texture texture = load(textureFilePath);
        preloadedTexturesMap.put(textureFilePath, texture);
        return texture;
    }

    private static Texture load(String path) {
        int[] pixels = null;
        FileInputStream stream;
        int width = 0;
        int height = 0;
        int id;

        try {
            stream = new FileInputStream(path);
            BufferedImage image = ImageIO.read(stream);
            width = image.getWidth();
            height = image.getHeight();
            pixels = new int[width * height];
            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (FileNotFoundException e) {
            Logger.error(String.format("Texture '<underline>%s</>' not found!", path), e);
            return null;
        } catch (SecurityException e) {
            Logger.error(String.format("Texture file access denied on path '<underline>%s</>'!", path), e);
            return null;
        } catch (IllegalArgumentException e) {
            Logger.error(String.format("Incorrect texture file path '<underline>%s</>'!", path), e);
            return null;
        } catch (IOException e) {
            Logger.error(String.format("Error while loading texture '<underline>%s</>'!", path), e);
            return null;
        }

        int[] data = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            int a = (pixels[i] & 0xff000000) >> 24;
            int r = (pixels[i] & 0xff0000) >> 16;
            int g = (pixels[i] & 0xff00) >> 8;
            int b = (pixels[i] & 0xff);

            data[i] = a << 24 | b << 16 | g << 8 | r;
        }

        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        GL11.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
        glBindTexture(GL_TEXTURE_2D, 0);

        return new Texture(width, height, id);
    }

}
