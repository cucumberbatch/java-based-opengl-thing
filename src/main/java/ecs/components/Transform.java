package ecs.components;

import ecs.util.Vector3;

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
        return  "position: "      + position.toString() +
                "\nrotation: "    + rotation.toString() +
                "\nscale: "       + scale.toString() + "\n";
    }
}
