package math.series.computing;

import math.series.FourierCore;

import static java.lang.Math.*;

public class CosineFourierCore implements FourierCore {
    @Override
    public double calculate(double x, double... a) {
        return cos((PI * x * a[0]) / a[1]);
    }
}
