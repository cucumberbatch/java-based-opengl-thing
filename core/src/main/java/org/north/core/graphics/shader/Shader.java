package org.north.core.graphics.shader;

import org.north.core.scene.component.MeshRenderer;
import org.north.core.scene.component.Transform;
import org.north.core.graphics.Graphics;
import org.north.core.asset.Asset;

public interface Shader extends Asset {
    void enable();
    void disable();
    boolean isEnabled();

    void updateUniforms(Graphics graphics, Transform transform, MeshRenderer renderer);
}
