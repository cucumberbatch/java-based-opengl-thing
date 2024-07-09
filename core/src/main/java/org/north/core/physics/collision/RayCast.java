package org.north.core.physics.collision;

import org.joml.Vector3f;
import org.north.core.architecture.entity.Entity;
import org.north.core.component.Transform;
import org.north.core.physics.Sphere;

public class RayCast {
    private final Vector3f origin;
    private final Vector3f direction;

    public RayCast(Vector3f origin, Vector3f direction) {
        this.origin = origin;
        this.direction = direction;
    }

    public boolean isIntersects(Entity entity) {
        Transform gTransform = entity.getTransform().getGlobalTransform();
        Vector3f gPosition = gTransform.getPosition();
        float gSize = gTransform.getScale().length();

        Sphere sphere = new Sphere(gPosition.x, gPosition.y, gPosition.z, gSize);
        return this.isIntersects(sphere);
    }

    // todo: rewrite for object-free usage
    public boolean isIntersects(Sphere sphere) {
        Vector3f sphereCenter = sphere.getCenter();
        float a = direction.dot(direction);
        Vector3f oc = origin.sub(sphereCenter, new Vector3f());
        float b = 2f * oc.dot(direction);
        float c = oc.dot(oc) - sphere.r * sphere.r;
        float discriminant = b * b - 4 * a * c;
        return discriminant >= 0;
    }

}
