package org.north.core.scene;

import org.joml.Vector3f;
import org.north.core.architecture.entity.Entity;
import org.north.core.architecture.tree.v2.TreeNode;
import org.north.core.component.Transform;
import org.north.core.management.data.OctreeNode;
import org.north.core.physics.collision.Collision;
import org.north.core.physics.collision.CollisionState;

import java.util.*;

public class Scene {
    public String name;

    public Scene(String sceneName) {
        this.name = sceneName;
    }

}
