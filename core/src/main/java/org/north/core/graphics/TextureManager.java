package org.north.core.graphics;

import org.north.core.utils.BufferUtils;
import org.north.core.utils.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

public class TextureManager {
    private static final Map<String, Texture> preloadedTexturesMap = new HashMap<>();

    public static Texture tryToLoadTexture(String textureFilePath) {
        Texture texture = preloadedTexturesMap.get(textureFilePath);
        if (texture != null) {
            return texture;
        }
        texture = load(textureFilePath);
        preloadedTexturesMap.put(textureFilePath, texture);
        return texture;
    }

    private static Texture load(String path) {
        int[] pixels;
        int width;
        int height;
        int id;

        try (FileInputStream stream = new FileInputStream(path)) {
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

        int totalPixelCount = width * height;

        for (int i = 0; i < totalPixelCount / 2; i++) {
            int reversedIndex = totalPixelCount - i - 1;
            int temp = pixels[reversedIndex];
            pixels[reversedIndex] = pixels[i];
            pixels[i] = temp;
        }

        id = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, id);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(pixels));
        glBindTexture(GL_TEXTURE_2D, 0);

        return new Texture(width, height, id);
    }

}
