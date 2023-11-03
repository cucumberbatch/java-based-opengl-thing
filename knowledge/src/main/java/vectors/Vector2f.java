package vectors;

import interpolation.Interpolation;

import java.util.Objects;

public class Vector2f {
    public float x, y;

    public Vector2f() {
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f other) {
        this.x = other.x;
        this.y = other.y;
    }

    public Vector2f set(float value) {
        this.x = value;
        this.y = value;
        return this;
    }

    public Vector2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2f set(Vector2f other) {
        this.x = other.x;
        this.y = other.y;
        return this;
    }

    public Vector2f add(float value) {
        this.x += value;
        this.y += value;
        return this;
    }

    public Vector2f add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2f add(Vector2f other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2f sub(float value) {
        this.x -= value;
        this.y -= value;
        return this;
    }

    public Vector2f sub(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2f sub(Vector2f other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector2f mul(float val) {
        this.x *= val;
        this.y *= val;
        return this;
    }

    public Vector2f div(float val) {
        this.x /= val;
        this.y /= val;
        return this;
    }

    public float dot(Vector2f other) {
        return this.x * other.x + this.y * other.y;
    }

    public float length() {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y);
    }

    public Vector2f normalized() {
        float factor = 1.0f / length();
        this.x *= factor;
        this.y *= factor;
        return this;
    }

    public static Vector2f lerp(Vector2f begin, Vector2f end, float ratio) {
        return new Vector2f(
                Interpolation.lerp(begin.x, end.x, ratio),
                Interpolation.lerp(begin.y, end.y, ratio)
        );
    }

    public static Vector2f lerpSmooth(Vector2f begin, Vector2f end, float beginSpeed, float endSpeed, float ratio) {
        return new Vector2f(
                Interpolation.lerpSmooth(begin.x, end.x, beginSpeed, endSpeed, ratio),
                Interpolation.lerpSmooth(begin.y, end.y, beginSpeed, endSpeed, ratio)
        );
    }

    public static void reset(Vector2f... vectors) {
        for (Vector2f vector: vectors) {
            vector.set(0, 0);
        }
    }

    public static Vector2f add(Vector2f that, Vector2f other) {
        return new Vector2f(that.x + other.x, that.y + other.y);
    }

    public static Vector2f sub(Vector2f that, Vector2f other) {
        return new Vector2f(that.x - other.x, that.y - other.y);
    }

    public static Vector2f mul(Vector2f that, float value) {
        return new Vector2f(that.x * value, that.y * value);
    }

    public static Vector2f div(Vector2f that, float value) {
        return new Vector2f(that.x / value, that.y / value);
    }

    public static float dot(Vector2f that, Vector2f other) {
        return that.x * other.x + that.y * other.y;
    }

    public static float length(Vector2f that) {
        return (float) Math.sqrt(that.x * that.x + that.y * that.y);
    }

    public static Vector2f normalized(Vector2f that) {
        float factor = 1.0f / that.length();
        return new Vector2f(
                that.x *= factor,
                that.y *= factor
        );
    }

    public static Vector2f zero()        { return new Vector2f( 0,  0); }
    public static Vector2f one()         { return new Vector2f( 1,  1); }
    public static Vector2f right()       { return new Vector2f( 1,  0); }
    public static Vector2f left()        { return new Vector2f(-1,  0); }
    public static Vector2f up()          { return new Vector2f( 0,  1); }
    public static Vector2f down()        { return new Vector2f( 0, -1); }

    @Override
    public String toString() {
        return String.format("[%-7.3f %-7.3f]\tV2f: %s", x, y, System.identityHashCode(this));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2f vector2 = (Vector2f) o;
        return Float.compare(vector2.x, x) == 0 &&
                Float.compare(vector2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

}
