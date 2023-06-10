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

    public static Matrix4f transpose(Matrix4f matrix) {
        Matrix4f transposedMatrix = new Matrix4f();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                transposedMatrix.elements[i + j * 4] = matrix.elements[j + i * 4];
            }
        }
        return transposedMatrix;
    }

    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f result = new Matrix4f();

        result.elements[0 + 0 * 4] =  1.0f / (right - left);
        result.elements[1 + 1 * 4] =  1.0f / (top - bottom);
        result.elements[2 + 2 * 4] = -1.0f / (far - near);

//        result.elements[3 + 0 * 4] = -(right + left) / (right - left);
//        result.elements[3 + 1 * 4] = -(top + bottom) / (top - bottom);
        result.elements[3 + 2 * 4] = -(far + near)   / (far - near);

        result.elements[3 + 3 * 4] = 1.0f;

        return result;
    }

    public static Matrix4f orthographic2(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f result = new Matrix4f();

        result.elements[0 + 0 * 4] =  2.0f * near / (right - left);
        result.elements[1 + 1 * 4] =  2.0f * near / (top - bottom);
        result.elements[3 + 2 * 4] =  1.0f;

        result.elements[3 + 0 * 4] =  (right + left) / (right - left);
        result.elements[3 + 1 * 4] =  (top + bottom) / (top - bottom);
        result.elements[2 + 2 * 4] = -(far + near) / (far - near);

        result.elements[3 + 3 * 4] = -2.0f * far * near / (far - near);

        return result;
    }

    public static Matrix4f orthographic3(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f result = new Matrix4f();

        result.elements[0 + 0 * 4] =  2.0f * near / (right - left);
        result.elements[1 + 1 * 4] =  2.0f * near / (top - bottom);
        result.elements[3 + 2 * 4] =  1.0f;

        result.elements[3 + 0 * 4] =  (right + left) / (right - left);
        result.elements[3 + 1 * 4] =  (top + bottom) / (top - bottom);
        result.elements[2 + 2 * 4] = -(far + near) / (far - near);

        result.elements[3 + 3 * 4] = -2.0f * far * near / (far - near);

        return result;
    }

    public static Matrix4f lerp(Matrix4f that, Matrix4f other, float ratio) {
        Matrix4f result = new Matrix4f();

        for (int i = 0; i < 16; i++)
            result.elements[i] = that.elements[i] + (other.elements[i] - that.elements[i]) * ratio;

        return result;
    }

    public static Matrix4f perspective(float fov, float near, float far, float ratio) {
        Matrix4f result = new Matrix4f();

        float d = (float) (1 / Math.tan(Math.toRadians(fov) / 2.0f));

        result.elements[0 + 0 * 4] = d / ratio;
        result.elements[1 + 1 * 4] = d;

        result.elements[2 + 2 * 4] = - (far + near) / (far - near);
        result.elements[3 + 2 * 4] = - 2 * (far * near) / (far - near);

        result.elements[2 + 3 * 4] = -1.0f;

        return result;
    }

    public static Matrix4f perspective2(float fov, float near, float far, float ratio) {
        float tangent = (float) Math.tan(Math.toDegrees(fov / 2));   // tangent of half fovY
        float height  = near   * tangent;                    // half height of near plane
        float width   = height * ratio;                      // half width of near plane

        return orthographic2(-width, width, -height, height, near, far);
    }

    public static Matrix4f translation(Vector3f position, Vector3f rotation, Vector3f scale) {
        Matrix4f result = new Matrix4f();

        Matrix4f translation = Matrix4f.identity();
        Matrix4f scaleMat    = Matrix4f.identity();
        Matrix4f rotationX   = Matrix4f.identity();
        Matrix4f rotationY   = Matrix4f.identity();
        Matrix4f rotationZ   = Matrix4f.identity();

        float radX = (float) Math.toRadians(rotation.x);
        float sinX = (float) Math.sin(radX);
        float cosX = (float) Math.cos(radX);

        float radY = (float) Math.toRadians(rotation.y);
        float sinY = (float) Math.sin(radY);
        float cosY = (float) Math.cos(radY);

        float radZ = (float) Math.toRadians(rotation.z);
        float sinZ = (float) Math.sin(radZ);
        float cosZ = (float) Math.cos(radZ);

        scaleMat.elements[0 + 0 * 4] = scale.x;
        scaleMat.elements[1 + 1 * 4] = scale.y;
        scaleMat.elements[2 + 2 * 4] = scale.z;

        // * cosY * cosZ;
        // * cosZ * cosX;
        // * cosX * cosY;

        rotationX.elements[1 + 1 * 4] =  cosX;
        rotationX.elements[2 + 1 * 4] =  sinX;
        rotationX.elements[1 + 2 * 4] = -sinX;
        rotationX.elements[2 + 2 * 4] =  cosX;

        rotationY.elements[0 + 0 * 4] =  cosY;
        rotationY.elements[0 + 2 * 4] =  sinY;
        rotationY.elements[2 + 0 * 4] = -sinY;
        rotationY.elements[2 + 2 * 4] =  cosY;

        rotationZ.elements[0 + 0 * 4] =  cosZ;
        rotationZ.elements[1 + 0 * 4] =  sinZ;
        rotationZ.elements[0 + 1 * 4] = -sinZ;
        rotationZ.elements[1 + 1 * 4] =  cosZ;

        translation.elements[3 + 0 * 4] = position.x;
        translation.elements[3 + 1 * 4] = position.y;
        translation.elements[3 + 2 * 4] = position.z;

//        result.elements[3 + 3 * 4] = 1f;

        return Matrix4f.multiply(Matrix4f.multiply(Matrix4f.multiply(Matrix4f.multiply(translation, scaleMat), rotationX), rotationY), rotationZ);
    }

    public static Matrix4f translation(Vector3f vector3) {
        Matrix4f result = identity();

        result.elements[0 + 3 * 4] = vector3.x;
        result.elements[1 + 3 * 4] = vector3.y;
        result.elements[2 + 3 * 4] = vector3.z;

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

//        return new Matrix4f(new float[]{
//                xAxis.x, xAxis.y, xAxis.z, eye.x,
//                yAxis.x, yAxis.y, yAxis.z, eye.y,
//                zAxis.x, zAxis.y, zAxis.z, eye.z,
//                0f,      0f,      0f,      1f
//        });

        return new Matrix4f(new float[]{
                xAxis.x, xAxis.y, xAxis.z, 0f,
                yAxis.x, yAxis.y, yAxis.z, 0f,
                zAxis.x, zAxis.y, zAxis.z, 0f,
                eye.x,   eye.y,   eye.z,   1f
        });

//        return new Matrix4f(new float[]{
//                xAxis.x, yAxis.x, zAxis.x, 0f,
//                xAxis.y, yAxis.y, zAxis.y, 0f,
//                xAxis.z, yAxis.z, zAxis.z, 0f,
//                Vector3f.dot(xAxis, Vector3f.negate(eye)),
//                Vector3f.dot(yAxis, Vector3f.negate(eye)),
//                Vector3f.dot(zAxis, Vector3f.negate(eye)),
//                1f
//        });
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
