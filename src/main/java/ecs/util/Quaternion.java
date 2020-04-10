package ecs.util;

public class Quaternion {
    public float x, y, z, w;

    public Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z + w * w);
    }

    public Quaternion normalize() {
        float length = length();

        x = x / length;
        y = y / length;
        z = z / length;
        w = w / length;

        return this;
    }

    public Quaternion conjugate() {
        return new Quaternion(-x, -y, -z, w);
    }

    public Quaternion mul(Quaternion r) {
        float w_ = w * r.w - x * r.x - y * r.y - z * r.z;
        float x_ = x * r.w + w * r.x - z * r.y + y * r.z;
        float y_ = y * r.w + z * r.x + w * r.y - x * r.z;
        float z_ = z * r.w - y * r.x + x * r.y + w * r.z;

        return new Quaternion(x_, y_, z_, w_);
    }


}
