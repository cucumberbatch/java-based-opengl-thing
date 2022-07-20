package math.matrix;

public interface LinearEquationsSystemSolver {
    double[] solve(double[][] matrix, double[] vector);
}
