package org.north.core.scene;

import org.north.core.scene.component.management.ComponentManager;
import org.north.core.scene.tree.TreeNode;

public interface SceneComposer {
    void compose(TreeNode<Entity> sceneRoot, ComponentManager componentManager);
}
