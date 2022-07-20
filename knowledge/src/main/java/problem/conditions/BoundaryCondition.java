package problem.conditions;

import problem.models.FieldConfiguration;

public interface BoundaryCondition {
    double u(double x, double t);

    default void apply(FieldConfiguration configuration) {
        for (int m = 0; m < configuration.m; m++) {
            for (int n = 0; n < configuration.n; n++) {
                configuration.matrix[m][n][0] = u(
                        (double) n * configuration.lengthStep,
                        (double) m * configuration.timeStep);
            }
        }
    }
}
