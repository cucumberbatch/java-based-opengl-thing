package ecs.components;

import ecs.math.Vector3;

/**
 * The main component of each game object that tells
 * you about its position, rotation and scale
 *
 * @author cucumberbatch
 */
public class Transform extends Component {
    public Vector3 position;
    public Vector3 rotation;
    public Vector3 scale;

    public Transform() {
        position = Vector3.zero();
        rotation = Vector3.zero();
        scale = Vector3.zero();
    }

    @Override
    public String toString() {
        return  "position:\t"   + position +
                "\nrotation:\t" + rotation +
                "\nscale:\t\t"  + scale + "\n";
    }
}
