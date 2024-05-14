package org.north.core.scene;

import org.north.core.architecture.entity.ComponentManager;
import org.north.core.architecture.entity.Entity;
import org.north.core.architecture.tree.v2.TreeNode;

public interface SceneComposer {
    void compose(TreeNode<Entity> sceneRoot, ComponentManager componentManager);
}
