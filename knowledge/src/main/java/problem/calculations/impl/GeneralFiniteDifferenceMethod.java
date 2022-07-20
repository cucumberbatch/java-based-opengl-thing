package problem.calculations.impl;

import problem.Constants;
import problem.calculations.FiniteDifferenceMethod;
import problem.solution.Solution;
import problem.models.FieldConfiguration;
import math.matrix.LinearEquationsSystemSolver;

public class GeneralFiniteDifferenceMethod implements FiniteDifferenceMethod {
    private LinearEquationsSystemSolver algorithm;
    private double weight;

    public GeneralFiniteDifferenceMethod(double weight, LinearEquationsSystemSolver algorithm) {
        this.algorithm = algorithm;
        this.weight = weight;
    }

    @Override
    public FieldConfiguration solve(FieldConfiguration conf) {
        Solution solution = new Solution();
        double[][][] matrix = conf.matrix;
        double gamma = Constants.a_sqr * conf.timeStep / conf.lengthStep / conf.lengthStep;
        double lambda  = (conf.lengthStep * conf.lengthStep) / conf.timeStep;
        double weight_inv = 1 / weight;

        double[][] A = new double[conf.n-2][conf.n-2];
        double[]   y = new double[conf.n-2];
        double[]   x;

        // Loop by all the time elements
        for (int m = 1; m < conf.m; m++) {
            // Loop by the length elements to fill the SoLE (System of Linear Equations)
            for (int n = 0; n < conf.n-2; n++) {
                // Collect data into left and top elements
                if (n > 0) {
                    A[n][n-1] = A[n-1][n] = Constants.a_sqr;
                }
                // Collect data into diagonal elements and y vector
                A[n][n] = -2 * (Constants.a_sqr + 0.5d * weight_inv * lambda);
                y[n] = 2 * weight_inv * ((1-weight) * Constants.a_sqr - 0.5d * lambda) * matrix[m-1][n+1][0]
                        - (1-weight) * weight_inv * Constants.a_sqr * (matrix[m-1][n][0] + matrix[m-1][n+2][0])
                        + weight_inv * conf.lengthStep * conf.lengthStep * solution.u(n * conf.lengthStep, (m-1) * conf.timeStep);
            }

            // Subtract from the first and last rows
            y[0]            -= Constants.a_sqr * matrix[m-1][0][0];
            y[y.length-1]   -= Constants.a_sqr * matrix[m-1][conf.n-1][0];

            // Solve the current SoLE
            x = algorithm.solve(A, y);

            // Insert calculated data
            System.arraycopy(x, 0, matrix[m], 1, conf.n - 2);
        }

        return null;
    }
}
