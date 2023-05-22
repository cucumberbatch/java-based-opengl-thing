package ecs.graphics;

public class Material {
    public long    id;
    public String  name;
    public Shader  shader;
    public Texture texture;

    public Material(String name, Shader shader, Texture texture) {
        this.name    = name;
        this.shader  = shader;
        this.texture = texture;
    }
}
