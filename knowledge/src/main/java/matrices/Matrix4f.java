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

    public Matrix4f mul(Matrix4f other) {
        float sum;
        for (int verticalIndex = 0; verticalIndex < 4; verticalIndex++) {
            for (int horizontalIndex = 0; horizontalIndex < 4; horizontalIndex++) {
                sum = 0f;
                for (int runnerIndex = 0; runnerIndex < 4; runnerIndex++) {
                    sum += this.elements[horizontalIndex + runnerIndex * 4] * other.elements[runnerIndex + verticalIndex * 4];
                }
                this.elements[horizontalIndex + verticalIndex * 4] = sum;
            }
        }
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
        Matrix4f result = Matrix4f.identity();

        result.elements[0 + 0 * 4] = 2.0f / (right - left);
        result.elements[1 + 1 * 4] = 2.0f / (top - bottom);
        result.elements[2 + 2 * 4] = 2.0f / (near - far);

        result.elements[0 + 3 * 4] = (left + right) / (left - right);
        result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
        result.elements[2 + 3 * 4] = (far + near) / (far - near);

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

    public static Matrix4f perspective3(float fov, float ratio, float near, float far) {
        Matrix4f result = Matrix4f.identity();

        float q = 1.0f / (float) Math.tan(Math.toRadians(fov / 2));
        float a =  q / ratio;
        float b = (near + far) / (near - far);
        float c = (2.0f * near * far) / (near - far);

        result.elements[0 + 0 * 4] = a;
        result.elements[1 + 1 * 4] = q;
        result.elements[2 + 2 * 4] = b;
        result.elements[3 + 2 * 4] = -1.0f;
        result.elements[2 + 3 * 4] = c;

        return result;
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

        return Matrix4f.mul(Matrix4f.mul(Matrix4f.mul(Matrix4f.mul(translation, scaleMat), rotationX), rotationY), rotationZ);
    }

    public static Matrix4f translation(Vector3f vector3) {
        Matrix4f result = identity();

        result.elements[0 + 3 * 4] = vector3.x;
        result.elements[1 + 3 * 4] = vector3.y;
        result.elements[2 + 3 * 4] = vector3.z;

        return result;
    }

    public static Matrix4f scale(Vector3f vector3) {
        Matrix4f result = identity();

        result.elements[0 + 0 * 4] = vector3.x;
        result.elements[1 + 1 * 4] = vector3.y;
        result.elements[2 + 2 * 4] = vector3.z;

        return result;
    }

    public static Matrix4f rotation(float angle, Vector3f axis) {
        Matrix4f result = Matrix4f.identity();

        float radians = (float) Math.toRadians(angle);
        float cos = (float) Math.cos(radians);
        float sin = (float) Math.cos(radians);
        float omc = 1.0f - cos;

        float x = axis.x;
        float y = axis.y;
        float z = axis.z;

        result.elements[0 + 0 * 4] = x * omc + cos;
        result.elements[1 + 0 * 4] = y * x * omc + z * sin;
        result.elements[2 + 0 * 4] = x * z * omc - y * sin;

        result.elements[0 + 1 * 4] = x * y * omc - z * sin;
        result.elements[1 + 1 * 4] = y * omc + cos;
        result.elements[2 + 1 * 4] = y * z * omc + x * sin;

        result.elements[0 + 2 * 4] = x * z * omc + y * sin;
        result.elements[1 + 2 * 4] = y * z * omc - x * sin;
        result.elements[2 + 2 * 4] = z * omc + cos;

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

    public static Matrix4f mul(Matrix4f that, Matrix4f other) {
        Matrix4f result = new Matrix4f();
        float sum;

        for (int verticalIndex = 0; verticalIndex < 4; verticalIndex++) {
            for (int horizontalIndex = 0; horizontalIndex < 4; horizontalIndex++) {
                sum = 0f;
                for (int runnerIndex = 0; runnerIndex < 4; runnerIndex++) {
                    sum += that.elements[horizontalIndex + runnerIndex * 4] * other.elements[runnerIndex + verticalIndex * 4];
                }
                result.elements[horizontalIndex + verticalIndex * 4] = sum;
            }
        }

        return result;
    }

    public static Matrix4f lookAt2(Vector3f eye, Vector3f at, Vector3f up) {
        Vector3f forward = new Vector3f().sub(eye, at).normalized();
        Vector3f left = new Vector3f().cross(up, forward).normalized();
        Vector3f top = new Vector3f().cross(forward, left);

        Matrix4f result = Matrix4f.identity();

        result.elements[0 + 0 * 4] = left.x;
        result.elements[0 + 1 * 4] = left.y;
        result.elements[0 + 2 * 4] = left.z;

        result.elements[1 + 0 * 4] = top.x;
        result.elements[1 + 1 * 4] = top.y;
        result.elements[1 + 2 * 4] = top.z;

        result.elements[2 + 0 * 4] = forward.x;
        result.elements[2 + 1 * 4] = forward.y;
        result.elements[2 + 2 * 4] = forward.z;

        result.elements[0 + 3 * 4] = -left.x * eye.x - left.y * eye.y - left.z * eye.z;
        result.elements[1 + 3 * 4] = -top.x * eye.x - top.y * eye.y - top.z * eye.z;
        result.elements[2 + 3 * 4] = -forward.x * eye.x - forward.y * eye.y - forward.z * eye.z;

        return result;
    }

    public static Matrix4f lookAt(Vector3f eye, Vector3f at, Vector3f up) {
        Vector3f zAxis = new Vector3f().sub(at, eye).normalized();
        Vector3f xAxis = new Vector3f().cross(up, zAxis).normalized();
        Vector3f yAxis = new Vector3f().cross(zAxis, xAxis);


//        return new Matrix4f(new float[]{
//                xAxis.x, xAxis.y, xAxis.z, eye.x,
//                yAxis.x, yAxis.y, yAxis.z, eye.y,
//                zAxis.x, zAxis.y, zAxis.z, eye.z,
//                0f,      0f,      0f,      1f
//        });

        return new Matrix4f(new float[]{
                xAxis.x, yAxis.x, zAxis.x, eye.x,
                xAxis.y, yAxis.y, zAxis.y, eye.y,
                xAxis.z, yAxis.z, zAxis.z, eye.z,
                0f,      0f,      0f,      1f
        });

//        return new Matrix4f(new float[]{
//                xAxis.x, xAxis.y, xAxis.z, 0f,
//                yAxis.x, yAxis.y, yAxis.z, 0f,
//                zAxis.x, zAxis.y, zAxis.z, 0f,
//                eye.x,   eye.y,   eye.z,   1f
//        });

//        return new Matrix4f(new float[]{
//                xAxis.x, yAxis.x, zAxis.x, 0f,
//                xAxis.y, yAxis.y, zAxis.y, 0f,
//                xAxis.z, yAxis.z, zAxis.z, 0f,
//                eye.x,   eye.y,   eye.z,   1f
//        });

//        return new Matrix4f(new float[]{
//                xAxis.x, yAxis.x, zAxis.x, 0f,
//                xAxis.y, yAxis.y, zAxis.y, 0f,
//                xAxis.z, yAxis.z, zAxis.z, 0f,
//                new Vector3f().dot(xAxis, new Vector3f().negate(eye)),
//                new Vector3f().dot(yAxis, new Vector3f().negate(eye)),
//                new Vector3f().dot(zAxis, new Vector3f().negate(eye)),
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
