package ecs.components;

import ecs.graphics.Mesh;
import ecs.graphics.Shader;
import ecs.graphics.Texture;
import org.joml.Vector4f;

public class Sandbox extends AbstractComponent {

    public Mesh mesh;
    public Shader shader;
    public Texture texture;
    public Vector4f color = new Vector4f();

}
