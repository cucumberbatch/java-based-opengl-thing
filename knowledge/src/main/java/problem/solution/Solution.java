package problem.solution;

import problem.Constants;

public class Solution {

    public double u(double x, double t) {
        return x < x_(t)
                ? Constants.U0 * Math.pow((1 - x / x_(t)), (2 / (1 - Constants.alpha)))
                : 0;
    }

    public double x_(double t) {
        return Constants.L * Math.sqrt(1 - Math.exp(-Constants.b1 * t));
    }
}
