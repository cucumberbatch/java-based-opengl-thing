package org.north.core.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Graphics {
    private static Graphics instance;

    public Window window;

    public Shader   previouslyRenderedShader = null;
    public Texture  previouslyRenderedTexture = null;

    public Matrix4f projection = new Matrix4f().identity();
    public Matrix4f view = new Matrix4f().lookAt(new Vector3f(0, 0, -1), new Vector3f(0, 0, 1), new Vector3f(0, 1, 0));

    // implement dependency injection
    public static Graphics getInstance(Window window) {
        if (instance == null) {
            instance = new Graphics(window);
        }
        return instance;
    }

    private Graphics(Window window) {
        this.window = window;
    }

    public void enableShader(Shader shader) {
        if (isPreviouslyRenderedShader(shader)) return;

        if (previouslyRenderedShader != null) {
            previouslyRenderedShader.disable();
        }

        previouslyRenderedShader = shader;
        shader.enable();
    }

    public void enableTexture(Texture texture) {
        if (texture == null) {
            previouslyRenderedTexture = null;
            return;
        }

        if (isPreviouslyRenderedTexture(texture)) return;

        if (previouslyRenderedTexture != null) {
            previouslyRenderedTexture.unbind();
        }

        previouslyRenderedTexture = texture;
        texture.bind();
    }

    private boolean isPreviouslyRenderedShader(Shader shader) {
        return previouslyRenderedShader != null && previouslyRenderedShader.getId() == shader.getId();
    }

    private boolean isPreviouslyRenderedTexture(Texture texture) {
        return previouslyRenderedTexture != null && previouslyRenderedTexture.getId() == texture.getId();
    }

}
