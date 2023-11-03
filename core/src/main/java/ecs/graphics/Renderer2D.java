package ecs.graphics;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

public class Renderer2D {

    public static void draw(VertexArray mesh, Texture texture, Shader shader) {
        texture.bind();
        shader.enable();
        mesh.render(GL_TRIANGLES);
        shader.disable();
        texture.unbind();
    }

    public static void draw(VertexArray mesh, Shader shader) {
        shader.enable();
        mesh.render(GL_TRIANGLES);
        shader.disable();
    }
}
