package math.series;

import math.types.Function;

import static java.lang.Math.*;

public class ExtendedFourier {
    private final int N;
    private double[] coefficients;


    public ExtendedFourier(int N) {
        this.N = N / 2;
    }

    public int amount() {
        return N;
    }

    public double[] transform(double[] discreteFunction, double a, double b) {
        int length = discreteFunction.length;
        double dx = (b - a) / (double) length;
        coefficients = new double[N];
        double x;

        for (int coefficientIndex = 0; coefficientIndex < N; coefficientIndex++) {
            for (int lengthIndex = 0; lengthIndex < length; lengthIndex++) {
                x = dx * lengthIndex;
                coefficients[coefficientIndex] += dx * discreteFunction[lengthIndex] * calculateCore(x, coefficientIndex, b);
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

    public double[] inverseTransform(double[] a, int length, double dx) {
        double[] function = new double[length];
        double steps = dx * (double) length;
        double x;

        for (int lengthIndex = 0; lengthIndex < length; lengthIndex++) {
            function[lengthIndex] = a[0] / 2;
            x = dx * lengthIndex;
            for (int coefficientIndex = 1; coefficientIndex < N; coefficientIndex++) {
//                function[lengthIndex] +=
//                        a[coefficientIndex] * cos((PI * x * coefficientIndex) / steps) +
//                        b[coefficientIndex] * sin((PI * x * coefficientIndex) / steps);
            }
        }
        return function;
    }

    private double calculateCore(double x, double... a) {
        return cos((PI * x * a[0]) / a[1]);
    }

}
