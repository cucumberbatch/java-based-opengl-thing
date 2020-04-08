package ecs.util;

public final class Vector3 {

    public float x;
    public float y;
    public float z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }



    public static float dot(Vector3 that, Vector3 other) {
        return (that.x * other.x + that.y * other.y + that.z * other.z);
    }


    public static Vector3 zero()        { return new Vector3( 0,  0,  0); }
    public static Vector3 one()         { return new Vector3( 1,  1,  1); }
    public static Vector3 up()          { return new Vector3( 0,  1,  0); }
    public static Vector3 down()        { return new Vector3( 0, -1,  0); }
    public static Vector3 forward()     { return new Vector3( 0,  0,  1); }
    public static Vector3 backward()    { return new Vector3( 0,  0, -1); }
    public static Vector3 right()       { return new Vector3( 1,  0,  0); }
    public static Vector3 left()        { return new Vector3(-1,  0,  0); }

}
