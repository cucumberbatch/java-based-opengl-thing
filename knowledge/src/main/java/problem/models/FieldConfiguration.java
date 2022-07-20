package problem.models;

import java.util.Arrays;
import java.util.Objects;

public class FieldConfiguration {
    public int m;               // Amount of time steps
    public int n;               // Amount of length steps
    public double[][][] matrix; // Field

    public double time;         // Time of modelling
    public double length;       // Length of field
    public double timeStep;     // Time step value
    public double lengthStep;   // Length step value
    public MultidimensionalMatrix  improvedMatrix;

    public FieldConfiguration(MultidimensionalMatrix matrix, double lengthStep, double timeStep) {
        this.timeStep = timeStep;
        this.lengthStep = lengthStep;
        this.time = m * timeStep;
        this.length = n * lengthStep;
        this.improvedMatrix = matrix;
    }

    public FieldConfiguration(int n, int m, double lengthStep, double timeStep) {
        this.m = m;
        this.n = n;
        this.timeStep = timeStep;
        this.lengthStep = lengthStep;
        this.time = m * timeStep;
        this.length = n * lengthStep;
        this.matrix = new double[m][n][2];
    }

    public FieldConfiguration(double[][] matrix, double lengthStep, double timeStep) {
//        this.matrix = matrix;
        this.lengthStep = lengthStep;
        this.timeStep = timeStep;
        this.m = matrix.length;
        this.n = matrix[0].length;
        this.time = m * timeStep;
        this.length = n * lengthStep;
    }

    public double[][] getPrintableData() {
        return getPrintableData(0);
    }

    public double[][] getPrintableData(int functionIndex) {
        double[][] data = new double[this.m][this.n + 2];

        // Print the line of length range
        data[0][this.n] = 0d;
        data[1][this.n] = this.length;
        data[2][this.n] = this.lengthStep;

        // Print the line of time range
        data[0][this.n + 1] = 0d;
        data[1][this.n + 1] = this.time;
        data[2][this.n + 1] = this.timeStep;

        // Print all table of data
        for (int timeIndex = this.m - 1; timeIndex >= 0; timeIndex--) {
            // Show current time step
            data[timeIndex][0] = timeIndex * this.timeStep;

            // Show the line of calculated data
            for (int lengthIndex = 0; lengthIndex < this.n; lengthIndex++) {
                data[timeIndex][lengthIndex] = this.matrix[timeIndex][lengthIndex][functionIndex];
            }
        }

        return data;
    }

    public MultidimensionalMatrix getMatrix() {
        return improvedMatrix;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldConfiguration that = (FieldConfiguration) o;
        return m == that.m &&
                n == that.n &&
                Double.compare(that.time, time) == 0 &&
                Double.compare(that.length, length) == 0 &&
                Double.compare(that.timeStep, timeStep) == 0 &&
                Double.compare(that.lengthStep, lengthStep) == 0 &&
                Arrays.equals(matrix, that.matrix);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(m, n, time, length, timeStep, lengthStep);
        result = 31 * result + Arrays.hashCode(matrix);
        return result;
    }

}
