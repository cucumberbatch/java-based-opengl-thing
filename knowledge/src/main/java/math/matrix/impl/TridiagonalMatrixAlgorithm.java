package math.matrix.impl;

import math.matrix.LinearEquationsSystemSolver;

public class TridiagonalMatrixAlgorithm implements LinearEquationsSystemSolver {
    @Override
    public double[] solve(double[][] matrix, double[] vector) {

        double[] x = new double[matrix.length];
        double[] a = new double[matrix.length];
        double[] c = new double[matrix.length];
        double[] b = new double[matrix.length];
        double[] f = new double[matrix.length];

        c[0] = matrix[0][0];
        f[0] = vector[0];
        for (int i = 1; i < matrix.length; i++) {
            b[i-1] = matrix[i-1][i];
            c[i] = matrix[i][i];
            a[i-1] = matrix[i][i-1];

            f[i] = vector[i];
        }

        double m;
        for (int i = 1; i < matrix.length; i++) {
            m = a[i-1] / c[i-1];
            c[i] = c[i] - m * b[i-1];
            f[i] = f[i] - m * f[i-1];
        }

        x[matrix.length-1] = f[matrix.length-1] / c[matrix.length-1];
        for (int i = matrix.length - 2; i >= 0; i--)
        {
            x[i] = (f[i] - b[i] * x[i+1]) / c[i];
        }


        //        double[] beta   = new double[matrix.length];
//        double[] alpha  = new double[matrix.length];
//        double[] gamma  = new double[matrix.length];
//        double[] x      = new double[matrix.length];
//
//        gamma[0] = matrix[0][0];
//        alpha[0] = -(matrix[0][1] / gamma[0]);
//        beta[0] = vector[0] / gamma[0];
//
//        for (int i = 1; i < matrix.length; i++) {
//            gamma[i] = matrix[i][i] + matrix[i][i-1] * alpha[i-1];
//            alpha[i] = -(matrix[i-1][i] / gamma[i]);
//            beta[i] = (vector[i] - matrix[i][i-1] * beta[i-1]) / gamma[i];
//        }
//
//        x[x.length-1] = beta[x.length-1];
//        for (int i = matrix.length - 2; i > -1; i--) {
//            x[i] = alpha[i] * x[i+1] + beta[i];
//        }

        return x;
    }
}
