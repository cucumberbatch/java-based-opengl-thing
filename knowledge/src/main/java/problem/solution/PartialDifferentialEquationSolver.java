package problem.solution;

import problem.models.ReactionDiffusionProblem;

public interface PartialDifferentialEquationSolver {
    void solve(ReactionDiffusionProblem problem);
}
