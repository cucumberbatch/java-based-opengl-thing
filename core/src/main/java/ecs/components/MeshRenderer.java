package ecs.components;

import ecs.graphics.Mesh;
import ecs.graphics.Shader;
import ecs.graphics.Texture;

public class MeshRenderer extends AbstractComponent {
    public Mesh    mesh;
    public Shader  shader;
    public Texture texture;
}
