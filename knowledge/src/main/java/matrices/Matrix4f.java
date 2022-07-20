package matrices;

import vectors.Vector3f;

import java.util.Arrays;

public class Matrix4f {

    private static final int SIZE = 4 * 4;
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

        result.elements[0 + 0 * 4] = 1.0f;
        result.elements[1 + 1 * 4] = 1.0f;
        result.elements[2 + 2 * 4] = 1.0f;
        result.elements[3 + 3 * 4] = 1.0f;

        return result;
    }

    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f result = new Matrix4f();

        result.elements[0 + 0 * 4] =  2.0f / (right - left);
        result.elements[1 + 1 * 4] =  2.0f / (top - bottom);
        result.elements[2 + 2 * 4] = -2.0f / (far - near);

        result.elements[3 + 0 * 4] = -(right + left) / (right - left);
        result.elements[3 + 1 * 4] = -(top + bottom) / (top - bottom);
        result.elements[3 + 2 * 4] = -(far + near) / (far - near);

        result.elements[3 + 3 * 4] = 1.0f;

        return result;
    }

    public static Matrix4f perspective(float fov, float near, float far, float ratio) {
        Matrix4f result = new Matrix4f();

        float d = (float) (1 / Math.tan(Math.toRadians(fov) / 2.0f));

        result.elements[0 + 0 * 4] = d / ratio;
        result.elements[1 + 1 * 4] = d;

        result.elements[2 + 2 * 4] = -(far + near) / (far - near);
        result.elements[3 + 2 * 4] = -(2 * far * near) / (far - near);

        result.elements[2 + 3 * 4] = -1.0f;

        return result;
    }

    public static Matrix4f translation(Vector3f vector3) {
        Matrix4f result = identity();

        result.elements[3 + 0 * 4] = vector3.x;
        result.elements[3 + 1 * 4] = vector3.y;
        result.elements[3 + 2 * 4] = vector3.z;

        return result;
    }


    public static Matrix4f rotateAroundOX(float angle) {
        Matrix4f result = identity();

        float rad = (float) Math.toRadians(angle);
        float sin = (float) Math.sin(rad);
        float cos = (float) Math.cos(rad);

        result.elements[1 + 1 * 4] = cos;
        result.elements[2 + 1 * 4] = sin;

        result.elements[1 + 2 * 4] = -sin;
        result.elements[2 + 2 * 4] = cos;

        return result;
    }

    public static Matrix4f rotateAroundOY(float angle) {
        Matrix4f result = identity();

        float rad = (float) Math.toRadians(angle);
        float sin = (float) Math.sin(rad);
        float cos = (float) Math.cos(rad);

        result.elements[0 + 0 * 4] = cos;
        result.elements[2 + 0 * 4] = sin;

        result.elements[0 + 2 * 4] = -sin;
        result.elements[2 + 3 * 4] = cos;

        return result;
    }

    public static Matrix4f rotateAroundOZ(float angle) {
        Matrix4f result = identity();

        float rad = (float) Math.toRadians(angle);
        float sin = (float) Math.sin(rad);
        float cos = (float) Math.cos(rad);

        result.elements[0 + 0 * 4] = cos;
        result.elements[1 + 0 * 4] = sin;

        result.elements[0 + 1 * 4] = -sin;
        result.elements[1 + 1 * 4] = cos;

        return result;
    }

    public static Matrix4f scale(Vector3f vector3) {
        Matrix4f result = identity();

        result.elements[0 + 0 * 4] = vector3.x;
        result.elements[1 + 1 * 4] = vector3.y;
        result.elements[2 + 2 * 4] = vector3.z;

        return result;
    }

    /**
     * Multiplies two 4x4 matrices with next pattern:
     * 
     * <p/><code>
     * <p>c1:1 = (a1:1 x b1:1) + (a1:2 x b2:1) + ... + (a1:4 x b4:1),</p>
     * <p>c1:2 = (a1:1 x b1:2) + (a1:2 x b2:2) + ... + (a1:4 x b4:2),</p>
     * <p>  ...  </p>
     * <p>c1:4 = (a1:1 x b1:4) + (a1:2 x b2:4) + ... + (a1:4 x b4:4),</p>
     * <p>c2:1 = (a2:1 x b1:1) + (a2:2 x b2:1) + ... + (a2:4 x b4:1),</p>
     * <p>c2:2 = (a2:1 x b1:2) + (a2:2 x b2:2) + ... + (a2:4 x b4:2),</p>
     * <p>  ...  </p>
     * <p>c4:4 = (a4:1 x b1:4) + (a4:2 x b2:4) + ... + (a4:4 x b4:4);</p>
     * </code>
     * where a1:1 , b1:1 and c1:1 are the first elements in first row of A, B and result C matrices respectively.
     * 
     * @param  that  a matrix A
     * @param  other a matrix B
     * @return       a result of A and B matrices multiplication
     */
    public static Matrix4f multiply(Matrix4f that, Matrix4f other) {
        Matrix4f result = new Matrix4f();
        float sum;

        for (int verticalIndex = 0; verticalIndex < 4; verticalIndex++) {
            for (int horizontalIndex = 0; horizontalIndex < 4; horizontalIndex++) {
                sum = 0f;
                for (int runnerIndex = 0; runnerIndex < 4; runnerIndex++) {
                    sum += that.elements[runnerIndex + verticalIndex * 4] * other.elements[horizontalIndex + runnerIndex * 4];
                }
                result.elements[horizontalIndex + verticalIndex * 4] = sum;
            }
        }

        return result;
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f at, Vector3f up) {
        Vector3f zAxis = Vector3f.normalized(Vector3f.sub(at, eye));
        Vector3f xAxis = Vector3f.normalized(Vector3f.cross(up, zAxis));
        Vector3f yAxis = Vector3f.cross(zAxis, xAxis);

        return new Matrix4f(new float[]{
                xAxis.x, xAxis.y, xAxis.z, eye.x,
                yAxis.x, yAxis.y, yAxis.z, eye.y,
                zAxis.x, zAxis.y, zAxis.z, eye.z,
                0f,      0f,      0f,      1f
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Matrix4f matrix4f = (Matrix4f) o;
        return Arrays.equals(elements, matrix4f.elements);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(elements);
    }

    @Override
    public String toString() {
        return Arrays.toString(elements) + "\tM4f: " + System.identityHashCode(this);
    }
}
