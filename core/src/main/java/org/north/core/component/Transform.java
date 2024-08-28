package org.north.core.component;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.north.core.physics.collision.MovementListener;

import java.util.Iterator;

/**
 * The main component of each game object that tells about its position, rotation and scale
 *
 * @author cucumberbatch
 */
public class Transform extends AbstractComponent {
    public Transform parent;

    private Vector3f position = new Vector3f(0, 0, 0);
    private Vector3f rotation = new Vector3f(0, 0, 0);
    private Vector3f scale    = new Vector3f(1, 1, 1);

    private static final MovementListener EMPTY_MOVEMENT_LISTENER = (e, p1, p2) -> {};

    private MovementListener movementListener = EMPTY_MOVEMENT_LISTENER;

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void moveTo(Vector3f position) {
        if (EMPTY_MOVEMENT_LISTENER != movementListener) {
            movementListener.registerMovement(this.getEntity(), new Vector3f(this.position), new Vector3f(position));
        }
        this.position.set(position);
    }

    public void moveTo(float x, float y, float z) {
        if (EMPTY_MOVEMENT_LISTENER != movementListener) {
            movementListener.registerMovement(this.getEntity(), new Vector3f(this.position), new Vector3f(x, y, z));
        }
        this.position.set(x, y, z);
    }

    public void moveRel(Vector3f position) {
        if (EMPTY_MOVEMENT_LISTENER != movementListener) {
            movementListener.registerMovement(this.getEntity(), new Vector3f(this.position), new Vector3f(position).add(this.position));
        }
        this.position.add(position);
    }

    public void moveRel(float x, float y, float z) {
        if (EMPTY_MOVEMENT_LISTENER != movementListener) {
            movementListener.registerMovement(this.getEntity(), new Vector3f(this.position), new Vector3f(x, y, z).add(this.position));
        }
        this.position.add(x, y, z);
    }

    public void rescaleTo(Vector3f scale) {
        this.scale.set(scale);
    }

    public void rescaleTo(float x, float y, float z) {
        this.scale.set(x, y, z);
    }

    public void rescaleRel(Vector3f scale) {
        this.scale.add(scale);
    }

    public void rescaleRel(float x, float y, float z) {
        this.scale.add(x, y, z);
    }

    public void setTransformListener(MovementListener movementListener) {
        this.movementListener = movementListener;
    }

    /**
     * Returns a local model matrix of this transform
     *
     * @apiNote memory consumption! matrix object creation on each call
     * @deprecated use method with matrix argument as temporary destination object
     * @return a local model matrix of this transform
     */
    public Matrix4f getLocalModelMatrix() {
        return getLocalModelMatrix(new Matrix4f());
    }

    public Matrix4f getLocalModelMatrix(Matrix4f destination) {
        return destination.identity()
                .translate(position.x, position.y, position.z)
                .rotateX(rotation.x)
                .rotateY(rotation.y)
                .rotateZ(rotation.z)
                .scale(scale.x, scale.y, scale.z);
    }

    public Matrix4f getGlobalModelMatrix(Matrix4f destination) {
        destination = destination.identity();
        for (Transform it = this; it != null; it = it.parent)
            destination.translate(it.position).rotateX(it.rotation.x).rotateY(it.rotation.y).rotateZ(it.rotation.z).scale(it.scale);
        return destination;
    }

    public Vector3f getGlobalPosition(Vector3f destination) {
        destination = destination.zero();
        for (Transform it = this; it != null; it = it.parent)
            destination.add(it.position);
        return destination;
    }

    public Vector3f getGlobalRotation(Vector3f destination) {
        destination = destination.zero();
        for (Transform it = this; it != null; it = it.parent)
            destination.rotateX(it.rotation.x).rotateY(it.rotation.y).rotateZ(it.rotation.z);
        return destination;
    }

    public Vector3f getGlobalScale(Vector3f destination) {
        return getGlobalModelMatrix(new Matrix4f()).getScale(destination);
    }

    public Transform getGlobalTransform() {
        Transform gTransform = new Transform();
        for (Transform it = this; it != null; it = it.parent) {
            gTransform.position.add(it.position);
            gTransform.rotation.add(it.rotation);
            gTransform.scale.set(gTransform.scale.x * it.scale.x, gTransform.scale.y * it.scale.y, gTransform.scale.z * it.scale.z);
        }
        return gTransform;
    }

    @Override
    public String toString() {
        return "Transform{" +
                "pos=" + position +
                ", rot=" + rotation +
                ", scl=" + scale +
                '}';
    }

    //    @Override
//    public String toString() {
//        return  "\nposition: " + position +
//                "\nrotation: " + rotation +
//                "\nscale:    " + scale +
//                super.toString();
//    }

    public Iterable<Transform> ascendantIterableTransform() {
        return new Iterable<>() {
            final Transform transform = Transform.this;

            @Override
            public Iterator<Transform> iterator() {
                return new Iterator<>() {
                    Transform next = transform;

                    @Override
                    public boolean hasNext() {
                        return next != null;
                    }

                    @Override
                    public Transform next() {
                        Transform transform = next;
                        next = next.parent;
                        return transform;
                    }
                };
            }
        };
    }

    /*
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(position);
        out.writeObject(rotation);
        out.writeObject(scale);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        position = (Vector3f) in.readObject();
        rotation = (Vector3f) in.readObject();
        scale = (Vector3f) in.readObject();
    }

     */

}
