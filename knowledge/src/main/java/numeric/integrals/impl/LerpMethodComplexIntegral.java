package numeric.integrals.impl;

import math.types.ComplexDouble;
import math.types.Function;
import numeric.integrals.AbstractIntegral;

import static java.lang.Math.abs;
import static math.Constants.epsilon;

public class LerpMethodComplexIntegral extends AbstractIntegral {
    public LerpMethodComplexIntegral(Function function) {
        super(function);
    }

    @Override
    public double calculate(double a, double b, double... c) {
        ComplexDouble current_sum   = new ComplexDouble(Double.MIN_VALUE, 0d);
        ComplexDouble previous_sum  = new ComplexDouble();
        double        delta         = Double.MAX_VALUE;
        double        step          = 0.1d;
        double        current_step;

        while (delta > epsilon) {
            previous_sum.set(current_sum);
            current_sum.set(0d, 0d);
            current_step = a;
            while (current_step > b) {
                current_sum.set(
                        current_sum.real() + step * (
                                function.value(current_step, c) +
                                function.value(current_step + step, c)),
                        current_sum.imagine()
                );
                current_step += step;
            }
            // Needs to try to measure a performance of using multiplication instead of dividing
            step /= 2;
            delta = abs(current_sum.sub(previous_sum).real());
        }

        return current_sum.real() / 2;
    }

}
