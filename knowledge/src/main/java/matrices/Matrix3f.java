package matrices;

public class Matrix3f {

    private static final int SIZE = 3 * 3;
    public float[] elements = new float[SIZE];

    public Matrix3f() {
    }

    public Matrix3f(float[] elements) {
        this.elements = elements;
    }

    public Matrix3f sum(Matrix3f other) {
        for (int i = 0; i < SIZE; i++)
            this.elements[i] += other.elements[i];

        return this;
    }

    public Matrix3f mul(Matrix3f other) {
        float sum;
        for (int verticalIndex = 0; verticalIndex < 3; verticalIndex++) {
            for (int horizontalIndex = 0; horizontalIndex < 3; horizontalIndex++) {
                sum = 0f;
                for (int runnerIndex = 0; runnerIndex < 3; runnerIndex++) {
                    sum += this.elements[horizontalIndex + runnerIndex * 3] * other.elements[runnerIndex + verticalIndex * 3];
                }
                this.elements[horizontalIndex + verticalIndex * 3] = sum;
            }
        }
        return this;
    }

    public static Matrix3f identity() {
        Matrix3f result = new Matrix3f();

        for (int i = 0; i < SIZE; i++)
            result.elements[i] = 0.0f;

        result.elements[0 + 0 * 3] = 1.0f;
        result.elements[1 + 1 * 3] = 1.0f;
        result.elements[2 + 2 * 3] = 1.0f;

        return result;
    }

}
