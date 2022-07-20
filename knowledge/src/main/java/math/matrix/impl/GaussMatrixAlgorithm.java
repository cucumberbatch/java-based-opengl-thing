package math.matrix.impl;

import math.matrix.LinearEquationsSystemSolver;

/**
 * Solves a quadratic matrices NxN of doubles
 */
public class GaussMatrixAlgorithm implements LinearEquationsSystemSolver {

    public float[] solve(float[] matrix, float[] vector) {
        int n = vector.length;

        return null;
    }

    @Override
    public double[] solve(double[][] matrix, double[] vector) {
        // Size of an matrix
        int n = matrix.length;

        // Linear system
        double[][] system = new double[n][n+1];

        // Concatenate matrix and vector into system
        for (int i = 0; i < n; i++) {
            System.arraycopy(matrix[i], 0, system[i], 0, n);
            system[i][n] = vector[i];
        }

        // Solution of system
        double[] x = new double[n];

        // Converting the system into a triangular shape
        for (int k = 1; k < n; k++) {
            for (int j = k; j < n; j++) {
                double m = system[j][k-1] / system[k-1][k-1];
                for (int i = 0; i < n+1; i++) {
                    system[j][i] = system[j][i] - m * system[k-1][i];
                }
            }
        }

        // Finding x's
        for (int i = n-1; i >= 0; i--) {
            x[i] = system[i][n] / system[i][i];
            for (int j = n-1; j > i; j--) {
                x[i] = x[i] - system[i][j] * x[j] / system[i][i];
            }
        }

        return x;
    }
}
