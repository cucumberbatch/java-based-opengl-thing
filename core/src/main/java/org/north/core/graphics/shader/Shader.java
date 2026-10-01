package org.north.core.graphics.shader;

import org.north.core.component.MeshRenderer;
import org.north.core.component.Transform;
import org.north.core.graphics.Graphics;
import org.north.core.management.Asset;

public interface Shader extends Asset {
    void enable();
    void disable();
    boolean isEnabled();

    void updateUniforms(Graphics graphics, Transform transform, MeshRenderer renderer);
}
