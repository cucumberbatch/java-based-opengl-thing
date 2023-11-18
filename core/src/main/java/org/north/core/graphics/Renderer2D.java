package org.north.core.graphics;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class Renderer2D {

    public static void draw(VertexArray mesh, Texture texture, OldShader shader) {
        texture.bind();
        shader.enable();
        mesh.render(GL_TRIANGLES);
        shader.disable();
        texture.unbind();
    }

    public static void draw(VertexArray mesh, OldShader shader) {
        shader.enable();
        mesh.render(GL_TRIANGLES);
        shader.disable();
    }
}
