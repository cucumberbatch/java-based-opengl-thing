package vectors;

import interpolation.Interpolation;

import java.util.Objects;

public final class Vector3f {
    public float x, y, z;


    public Vector3f() { }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public Vector3f set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3f set(Vector3f other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
        return this;
    }

    public Vector3f add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3f add(Vector3f other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
        return this;
    }

    public Vector3f sub(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector3f sub(Vector3f other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
        return this;
    }

    public Vector3f mul(float val) {
        this.x *= val;
        this.y *= val;
        this.z *= val;
        return this;
    }

    public Vector3f div(float val) {
        this.x /= val;
        this.y /= val;
        this.z /= val;
        return this;
    }

    public float dot(Vector3f other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vector3f negate() {
        this.x = -this.x;
        this.y = -this.y;
        this.z = -this.z;
        return this;
    }

    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public Vector3f normalized() {
        return this.mul(1.0f / length());
    }

    public static void reset(Vector3f... vectors) {
        for (Vector3f vector: vectors) {
            vector.set(0, 0, 0);
        }
    }

    public static Vector3f add(Vector3f that, Vector3f other) {
        return new Vector3f(that.x + other.x, that.y + other.y, that.z + other.z);
    }

    public static Vector3f sub(Vector3f that, Vector3f other) {
        return new Vector3f(that.x - other.x, that.y - other.y, that.z - other.z);
    }

    public static Vector3f mul(Vector3f that, float value) {
        return new Vector3f(that.x * value, that.y * value, that.z * value);
    }

    public static Vector3f div(Vector3f that, float value) {
        return new Vector3f(that.x / value, that.y / value, that.z / value);
    }

    public static float dot(Vector3f that, Vector3f other) {
        return that.x * other.x + that.y * other.y + that.z * other.z;
    }

    public static Vector3f negate(Vector3f that) {
        return new Vector3f(-that.x, -that.y, -that.z);
    }

    public static float length(Vector3f that) {
        return (float) Math.sqrt(that.x * that.x + that.y * that.y + that.z * that.z);
    }

    public static Vector3f normalized(Vector3f that) {
        float factor = 1.0f / that.length();
        return new Vector3f(
            that.x *= factor,
            that.y *= factor,
            that.z *= factor
        );
    }

    public static Vector3f cross(Vector3f that, Vector3f other) {
        return new Vector3f(
                that.y * other.z - that.z * other.y,
                that.z * other.x - that.x * other.z,
                that.x * other.y - that.y * other.x
        );
    }

    public static Vector3f lerp(Vector3f begin, Vector3f end, float ratio) {
        return new Vector3f(
                Interpolation.lerp(begin.x, end.x, ratio),
                Interpolation.lerp(begin.y, end.y, ratio),
                Interpolation.lerp(begin.z, end.z, ratio)
        );
    }

    public static Vector3f zero()        { return new Vector3f( 0,  0,  0); }
    public static Vector3f one()         { return new Vector3f( 1,  1,  1); }
    public static Vector3f right()       { return new Vector3f( 1,  0,  0); }
    public static Vector3f left()        { return new Vector3f(-1,  0,  0); }
    public static Vector3f up()          { return new Vector3f( 0,  1,  0); }
    public static Vector3f down()        { return new Vector3f( 0, -1,  0); }
    public static Vector3f forward()     { return new Vector3f( 0,  0,  1); }
    public static Vector3f backward()    { return new Vector3f( 0,  0, -1); }

    @Override
    public String toString() {
         return String.format("[%-7.3f %-7.3f %-7.3f]\tV3f: %s", x, y, z, System.identityHashCode(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3f vector3 = (Vector3f) o;
        return Float.compare(vector3.x, x) == 0 &&
                Float.compare(vector3.y, y) == 0 &&
                Float.compare(vector3.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

}
