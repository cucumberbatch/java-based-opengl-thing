package org.north.core.components;

import org.north.core.shapes.Rectangle;
import org.joml.Vector2f;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MeshCollider extends AbstractComponent {
    public Rectangle body = new Rectangle(new Vector2f().zero(), new Vector2f().zero());
    public boolean isStatic = false;

    @Override
    protected void serialize(ObjectOutputStream out) throws IOException {

    }

    @Override
    protected void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {

    }
}
