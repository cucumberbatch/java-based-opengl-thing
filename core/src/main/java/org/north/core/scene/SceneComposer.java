package org.north.core.scene;

import org.north.core.component.management.ComponentManager;
import org.north.core.entity.Entity;
import org.north.core.entity.tree.TreeNode;

public interface SceneComposer {
    void compose(TreeNode<Entity> sceneRoot, ComponentManager componentManager);
}
