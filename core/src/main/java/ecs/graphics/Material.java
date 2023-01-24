package ecs.graphics;

public class Material {
    private long id;
    private String name;
    private Shader shader;

    public Material(String name, Shader shader) {
        this.name = name;
        this.shader = shader;
    }
}
