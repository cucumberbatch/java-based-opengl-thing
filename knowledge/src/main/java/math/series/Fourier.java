package math.series;

import math.types.Function;

public class Fourier implements Series {
    private final FourierCore core;
    private final int N;


    public Fourier(FourierCore core, int N) {
        this.core = core;
        this.N = N / 2;
    }

    public int amount() {
        return N;
    }

    public double[] transform(double[] discreteFunction, double a, double b) {
        int length = discreteFunction.length;
        double dx = (b - a) / (double) length;
        double[] coefficients = new double[N];
        double x;

        for (int coefficientIndex = 0; coefficientIndex < N; coefficientIndex++) {
            for (int lengthIndex = 0; lengthIndex < length; lengthIndex++) {
                x = dx * lengthIndex;
                coefficients[coefficientIndex] += dx * discreteFunction[lengthIndex] * core.calculate(x, coefficientIndex, b);
            }
            coefficients[coefficientIndex] *= 2.0d / b;
        }
        return coefficients;
    }

    public double[] transform(Function function, double a, double b) {
        int length = N * 2;
        double[] discreteFunction = new double[length];
        double dx = (b - a) / length;
        for (int i = 0; i < length; i++) {
            discreteFunction[i] = function.value(a + dx * i);
        }
        return transform(discreteFunction, a, b);
    }

    public double[] inverseTransform(double[] coefficients, int length, double dx) {
        double[] function = new double[length];
        double b = dx * (double) length;
        double x;

        for (int lengthIndex = 0; lengthIndex < length; lengthIndex++) {
            function[lengthIndex] = coefficients[0] / 2;
            x = dx * lengthIndex;
            for (int coefficientIndex = 1; coefficientIndex < N; coefficientIndex++) {
                function[lengthIndex] += coefficients[coefficientIndex] * core.calculate(x, coefficientIndex, b);
            }
        }
        return function;
    }
}
