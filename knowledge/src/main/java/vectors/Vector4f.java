package vectors;

import java.util.Objects;
import interpolation.Interpolation;

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

    public static Vector4f lerp(Vector4f begin, Vector4f end, float ratio) {
        return new Vector4f(
                Interpolation.lerp(begin.x, end.x, ratio),
                Interpolation.lerp(begin.y, end.y, ratio),
                Interpolation.lerp(begin.z, end.z, ratio),
                Interpolation.lerp(begin.w, end.w, ratio)
        );
    }

    public static Vector4f lerpSmooth(Vector4f begin, Vector4f end, float beginSpeed, float endSpeed, float ratio) {
        return new Vector4f(
                Interpolation.lerpSmooth(begin.x, end.x, beginSpeed, endSpeed, ratio),
                Interpolation.lerpSmooth(begin.y, end.y, beginSpeed, endSpeed, ratio),
                Interpolation.lerpSmooth(begin.z, end.z, beginSpeed, endSpeed, ratio),
                Interpolation.lerpSmooth(begin.w, end.w, beginSpeed, endSpeed, ratio)
        );
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

    @Override
    public String toString() {
        return String.format("[ %f,\t %f,\t %f,\t %f]", x, y, z, w);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector4f vector4f = (Vector4f) o;
        return Float.compare(vector4f.x, x) == 0 &&
                Float.compare(vector4f.y, y) == 0 &&
                Float.compare(vector4f.z, z) == 0 &&
                Float.compare(vector4f.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }
}
