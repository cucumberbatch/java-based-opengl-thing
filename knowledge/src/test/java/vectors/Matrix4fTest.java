package vectors;

import matrices.Matrix4f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Matrix4fTest {

    Matrix4f scale = new Matrix4f(new float[]{
            2, 0, 0, 0,
            0, 2, 0, 0,
            0, 0, 2, 0,
            0, 0, 0, 1});

    Matrix4f rotation = new Matrix4f(new float[]{
            0.5f,  0.25f, 0, 0,
            -0.25f, 0.5f,  0, 0,
            0,     0,     1, 0,
            0,     0,     0, 1});

    Matrix4f position = new Matrix4f(new float[]{
            1, 0, 0, 2,
            0, 1, 0, 4,
            0, 0, 1, 5,
            0, 0, 0, 1});


    Matrix4f testMatrix1 = new Matrix4f(new float[]{
            1, 1, 1, 2,
            2, 1, 1, 1,
            1, 2, 1, 1,
            1, 1, 2, 1});

    Matrix4f testMatrix2 = new Matrix4f(new float[]{
            1, 2, 3, 4,
            2, 3, 4, 5,
            3, 4, 5, 6,
            4, 5, 6, 7});


    Matrix4f testMultiplicationMatrices1and2 = new Matrix4f(new float[]{
            14, 19, 24, 29,
            11, 16, 21, 26,
            12, 17, 22, 27,
            13, 18, 23, 28});


    Matrix4f testPositionAfterTranslation1 = new Matrix4f(new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1});


    Vector3f testVectorForTranslation1 = new Vector3f(0f, 0f, 0f);


    @Test
    public void matrixTransformTest1() {
        Matrix4f result = position.translation(testVectorForTranslation1);

        assertEquals(testPositionAfterTranslation1, result);
    }


//    @Test
//    public void matrixMultiplicationTest1() {
//        Matrix4f expected = new Matrix4f(new float[]{
//                1,    0.5f, 0, 4,
//                -0.5f, 1,    0, 8,
//                0,    0,    2, 10,
//                0,    0,    0, 1});
//
//        Matrix4f result = Matrix4f.mul(
//                Matrix4f.mul(scale, position),
//                rotation
//        );
//
//        assertEquals(expected, result);
//    }

//    @Test
    public void matrixMultiplicationTest2() {
        Matrix4f expected = new Matrix4f(new float[]{
                2, 0, 0, 2,
                0, 2, 0, 4,
                0, 0, 2, 5,
                0, 0, 0, 1});

        Matrix4f result = Matrix4f.mul(position, scale);

        assertEquals(expected, result);
    }

    @Test
    public void orthoProjectionMatrixTest() {
        Matrix4f expected = new Matrix4f(new float[]{

        });
    }

//    @Test
    public void testMultiplication1() {
        Matrix4f result = Matrix4f.mul(testMatrix1, testMatrix2);

        assertEquals(testMultiplicationMatrices1and2, result);
    }

}