package ecs.managment;

import ecs.managment.memory.IPool;
import ecs.managment.memory.Pool;
import ecs.math.Matrix4f;
import ecs.math.Vector3f;

public class ObjectsStorage {

    public static IPool<Vector3f> vector3fPool = new Pool<>(1000, Vector3f::new);
    public static IPool<Matrix4f> matrix4fPool = new Pool<>(1000, Matrix4f::new);


}
