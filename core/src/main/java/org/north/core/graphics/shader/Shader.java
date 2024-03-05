package org.north.core.graphics.shader;

import org.north.core.component.MeshRenderer;
import org.north.core.graphics.Graphics;

public interface Shader {
    int getId();

    void enable();
    void disable();
    boolean isEnabled();

    void updateUniforms(Graphics graphics, MeshRenderer renderer);
}
