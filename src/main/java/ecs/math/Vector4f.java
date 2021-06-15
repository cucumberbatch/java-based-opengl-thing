package ecs.math;

public class Vector4f {
    public float x, y, z, w;


    public Vector4f() { }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f(Vector4f other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        this.w = other.w;
    }

    public Vector4f set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public Vector4f set(Vector4f other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        this.w = other.w;
        return this;
    }

    public Vector4f add(float x, float y, float z, float w) {
        this.x += x;
        this.y += y;
        this.z += z;
        this.w += w;
        return this;
    }

    public Vector4f add(Vector4f other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        this.w += other.w;
        return this;
    }

    public Vector4f sub(float x, float y, float z, float w) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        this.w -= w;
        return this;
    }

    public Vector4f sub(Vector4f other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        this.w -= other.w;
        return this;
    }

    public Vector4f mul(float val) {
        this.x *= val;
        this.y *= val;
        this.z *= val;
        this.w *= val;
        return this;
    }

    public Vector4f div(float val) {
        this.x /= val;
        this.y /= val;
        this.z /= val;
        this.w /= val;
        return this;
    }

    public float dot(Vector4f other) {
        return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
    }

    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w);
    }

    public Vector4f normalized() {
        float factor = 1.0f / length();
        this.x *= factor;
        this.y *= factor;
        this.z *= factor;
        this.w *= factor;
        return this;
    }

//    public static Vector4f rotate(Vector4f that, float angle, Vector3f axis) {
//
//    }

    public static Vector4f zero()        { return new Vector4f( 0.0f,  0.0f,  0.0f, 0.0f); }
    public static Vector4f one()         { return new Vector4f( 1.0f,  1.0f,  1.0f, 1.0f); }
    public static Vector4f right()       { return new Vector4f( 1.0f,  0.0f,  0.0f, 0.0f); }
    public static Vector4f left()        { return new Vector4f(-1.0f,  0.0f,  0.0f, 0.0f); }
    public static Vector4f up()          { return new Vector4f( 0.0f,  1.0f,  0.0f, 0.0f); }
    public static Vector4f down()        { return new Vector4f( 0.0f, -1.0f,  0.0f, 0.0f); }
    public static Vector4f forward()     { return new Vector4f( 0.0f,  0.0f,  1.0f, 0.0f); }
    public static Vector4f backward()    { return new Vector4f( 0.0f,  0.0f, -1.0f, 0.0f); }

}
