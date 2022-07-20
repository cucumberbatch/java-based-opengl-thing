package numeric.integrals.impl;

import numeric.integrals.AbstractIntegral;
import math.types.Function;

import static java.lang.Math.abs;
import static math.Constants.epsilon;

public class LerpMethodIntegral extends AbstractIntegral {
    public LerpMethodIntegral(Function function) {
        super(function);
    }

    @Override
    public double calculate(double a, double b, double... c) {
        double delta = Double.MAX_VALUE;
        double current_sum = Double.MAX_VALUE;
        double previous_sum;
        double current_step;
        double step = 0.1d;

        while (delta > epsilon) {
            previous_sum = current_sum;
            current_sum = 0;
            current_step = a;
            while (current_step < b) {
                current_sum += step * (function.value(current_step, c) + function.value(current_step + step, c));
                current_step += step;
            }
            // Needs to try to measure a performance of using multiplication instead of dividing
            step /= 2;
            delta = abs(current_sum - previous_sum);
        }

        return current_sum / 2;
    }
}
