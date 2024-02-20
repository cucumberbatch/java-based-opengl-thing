package org.north.core.component;

import org.north.core.graphics.Mesh;
import org.north.core.graphics.Texture;
import org.joml.Vector4f;
import org.north.core.graphics.shader.Shader;

public class GasCloud extends AbstractComponent {

    public Mesh mesh;
    public Shader shader;
    public Texture texture;
    public Vector4f color = new Vector4f();

}
