package ecs.math;

import ecs.utils.BufferUtils;
import ecs.utils.TerminalUtils;

import java.nio.FloatBuffer;

public class Matrix4f {

    private static final int SIZE = 4 * 4;

    public static final int FIRST_ELEMENT  = 0;
    public static final int SECOND_ELEMENT = 1;
    public static final int THIRD_ELEMENT  = 2;
    public static final int FOURTH_ELEMENT = 3;

    public static final int FIRST_ROW  = 0;
    public static final int SECOND_ROW = 4;
    public static final int THIRD_ROW  = 8;
    public static final int FOURTH_ROW = 12;

    public float[] elements = new float[SIZE];

    public Matrix4f() {
    }

    public Matrix4f(float[] elements) {
        this.elements = elements;
    }

    public Matrix4f sum(Matrix4f other) {
        for (int i = 0; i < SIZE; i++)
            this.elements[i] += other.elements[i];

        return this;
    }

    public static Matrix4f identity() {
        Matrix4f result = new Matrix4f();

        for (int i = 0; i < SIZE; i++)
            result.elements[i] = 0.0f;

        result.elements[ FIRST_ELEMENT  + FIRST_ROW  ] = 1.0f;
        result.elements[ SECOND_ELEMENT + SECOND_ROW ] = 1.0f;
        result.elements[ THIRD_ELEMENT  + THIRD_ROW  ] = 1.0f;
        result.elements[ FOURTH_ELEMENT + FOURTH_ROW ] = 1.0f;

        return result;
    }

    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f result = new Matrix4f();

        result.elements[0 + 0 * 4] = 2.0f / (right - left);
        result.elements[1 + 1 * 4] = 2.0f / (top - bottom);
        result.elements[2 + 2 * 4] = 2.0f / (near - far);

        result.elements[3 + 0 * 4] = (left + right) / (left - right);
        result.elements[3 + 1 * 4] = (bottom + top) / (bottom - top);
        result.elements[3 + 2 * 4] = (far + near) / (far - near);

        result.elements[3 + 3 * 4] = 1.0f;

        return result;
    }

    public static Matrix4f perspective(float fov, float near, float far, float ratio) {
        Matrix4f result = new Matrix4f();

        float d = (float) (1 / Math.tan(Math.toRadians(fov) / 2.0f));

        result.elements[ FIRST_ELEMENT  + FIRST_ROW  ] = d;
        result.elements[ SECOND_ELEMENT + SECOND_ROW ] = d;

        result.elements[ THIRD_ELEMENT  + THIRD_ROW  ] = -far / (far - near);
        result.elements[ FOURTH_ELEMENT + THIRD_ROW  ] = -far * near / (far - near);

        result.elements[ THIRD_ELEMENT  + FOURTH_ROW ] = -1.0f;

        return result;
    }

    public static Matrix4f translation(Vector3f vector3) {
        Matrix4f result = identity();

        result.elements[ FOURTH_ELEMENT + FIRST_ROW  ] = vector3.x;
        result.elements[ FOURTH_ELEMENT + SECOND_ROW ] = vector3.y;
        result.elements[ FOURTH_ELEMENT + THIRD_ROW  ] = vector3.z;

        return result;
    }

    public static Matrix4f rotation(Vector3f axis, float angle) {
        Matrix4f result = identity();

        float rad = (float) Math.toRadians(angle);
        float sin = (float) Math.sin(rad);
        float cos = (float) Math.cos(rad);

        if (axis.x > 0f && axis.y == 0f && axis.z == 0f) {
            result.elements[1 + 1 * 4] = cos;
            result.elements[2 + 1 * 4] = sin;

            result.elements[1 + 2 * 4] = -sin;
            result.elements[2 + 2 * 4] = cos;
        }
        else if (axis.x == 0f && axis.y > 0f && axis.z == 0f) {
            result.elements[0 + 0 * 4] = cos;
            result.elements[2 + 0 * 4] = sin;

            result.elements[0 + 2 * 4] = -sin;
            result.elements[2 + 3 * 4] = cos;
        }
        else if (axis.x == 0f && axis.y == 0f && axis.z > 0f) {
            result.elements[0 + 0 * 4] = cos;
            result.elements[1 + 0 * 4] = sin;

            result.elements[0 + 1 * 4] = -sin;
            result.elements[1 + 1 * 4] = cos;
        }
        else throw new IllegalArgumentException("Using multiple axis rotation at single rotation call");

        return result;
    }

    public static Matrix4f scale(Vector3f vector3) {
        Matrix4f result = identity();

        result.elements[ FIRST_ELEMENT  + FIRST_ROW  ] = vector3.x;
        result.elements[ SECOND_ELEMENT + SECOND_ROW ] = vector3.y;
        result.elements[ THIRD_ELEMENT  + THIRD_ROW  ] = vector3.z;

        return result;
    }

    public static Matrix4f multiply(Matrix4f that, Matrix4f other) {
        Matrix4f result = new Matrix4f();

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                float sum = 0.0f;
                for (int e = 0; e < 4; e++) {
                    sum += that.elements[x + e * 4] * other.elements[e + y * 4];
                }
                result.elements[x + y * 4] = sum;
            }
        }

        return result;
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f at, Vector3f up) {
        Vector3f zAxis = Vector3f.normalized(Vector3f.sub(at, eye));
        Vector3f xAxis = Vector3f.normalized(Vector3f.cross(zAxis, up));
        Vector3f yAxis = Vector3f.cross(xAxis, zAxis);

        Vector3f.negate(zAxis);

        return new Matrix4f(new float[]{
                xAxis.x, xAxis.y, xAxis.z, -Vector3f.dot(xAxis, eye),
                yAxis.x, yAxis.y, yAxis.z, -Vector3f.dot(yAxis, eye),
                zAxis.x, zAxis.y, zAxis.z, -Vector3f.dot(zAxis, eye),
                0f,      0f,      0f,      1f
        });
    }

    public FloatBuffer toFloatBuffer() {
        return BufferUtils.createFloatBuffer(elements);
    }


    @Override
    public String toString() {
        return TerminalUtils.formatOutputMatrix(this);
    }
}
