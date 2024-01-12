package org.north.core.components;

import org.north.core.graphics.Mesh;
import org.north.core.graphics.OldShader;
import org.north.core.graphics.Texture;
import org.joml.Vector4f;

public class GasCloud extends AbstractComponent {

    public Mesh mesh;
    public OldShader shader;
    public Texture texture;
    public Vector4f color = new Vector4f();

}
