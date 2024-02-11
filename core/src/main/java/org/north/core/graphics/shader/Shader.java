package org.north.core.graphics.shader;

import org.north.core.components.MeshRenderer;
import org.north.core.graphics.Graphics;

public interface Shader {
    int getId();

    void enable();
    void disable();
    boolean isEnabled();

    void prepareShader(Graphics graphics, MeshRenderer renderer);
}
