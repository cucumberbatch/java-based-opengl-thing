package org.north.core.graphics;

public class Material {
    public long    id;
    public String  name;
    public OldShader shader;
    public Texture texture;

    public Material(String name, OldShader shader, Texture texture) {
        this.name    = name;
        this.shader  = shader;
        this.texture = texture;
    }
}
