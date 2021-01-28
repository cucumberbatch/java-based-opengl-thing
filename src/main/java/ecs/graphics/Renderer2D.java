package ecs.graphics;

public class Renderer2D {

    public static void draw(VertexArray mesh, Texture texture, Shader shader) {
        texture.bind();
        shader.enable();
        mesh.render();
        shader.disable();
        texture.unbind();
    }
}
