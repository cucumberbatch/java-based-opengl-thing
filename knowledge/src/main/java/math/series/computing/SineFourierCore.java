package math.series.computing;

import math.series.FourierCore;

import static java.lang.Math.*;

public class SineFourierCore implements FourierCore {
    @Override
    public double calculate(double x, double... a) {
        return sin((PI * x * a[0]) / a[1]);
    }
}
