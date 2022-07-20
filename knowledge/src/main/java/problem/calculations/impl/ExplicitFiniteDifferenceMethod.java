package problem.calculations.impl;

import problem.Constants;
import problem.calculations.FiniteDifferenceMethod;
import problem.solution.Solution;
import problem.models.FieldConfiguration;
import problem.Constants.LogMessage;

import static problem.Constants.LOGGER;

public final class ExplicitFiniteDifferenceMethod implements FiniteDifferenceMethod {

    @Override
    public FieldConfiguration solve(FieldConfiguration conf) {
        Solution solution = new Solution();
        double[][] matrix = conf.matrix[0];
        double gamma = Constants.a_sqr * conf.timeStep / conf.lengthStep / conf.lengthStep;

        if (gamma > 0.5d) {
            LOGGER.warning(LogMessage.DIFF_METHOD_INACCURACY.getMessageString() + " :\tgamma = " + gamma);
        }

        for (int m = 0; m < conf.m-1; m++) {
            for (int n = 1; n < conf.n-1; n++) {
                matrix[m+1][n] = (
                        matrix[m][n-1] * gamma +                    // left bottom point
                        matrix[m][n+1] * gamma +                    // right bottom point
                        matrix[m][n  ] * (1 - 2 * gamma) +          // center bottom point
                        solution.u(n * conf.lengthStep, m * conf.timeStep) * conf.timeStep
                );
            }
        }

        return new FieldConfiguration(matrix, conf.lengthStep, conf.timeStep);
    }
}
