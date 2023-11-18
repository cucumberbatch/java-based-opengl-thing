package org.north.core.graphics;

import org.north.core.components.MeshRenderer;

public interface Shader {
    int getId();

    void enable();
    void disable();
    boolean isEnabled();

    void prepareShader(Graphics graphics, MeshRenderer renderer);
}
