package problem.calculations;

import problem.models.FieldConfiguration;

/**
 * Interface for implementing difference methods
 */
public interface FiniteDifferenceMethod {
    FieldConfiguration solve(FieldConfiguration configuration);
}
