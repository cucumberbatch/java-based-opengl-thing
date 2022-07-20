package problem.models;

import math.types.Function;
import problem.solution.PartialDifferentialEquationSolver;
import view.DataPrinter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReactionDiffusionProblemBuilder {
    private ReactionDiffusionProblem problem;
    private PartialDifferentialEquationSolver solver;

    public ReactionDiffusionProblemBuilder grid(double length, int n, double time, int m) {
        this.grid(new FieldConfiguration(new double[m + 1][n + 1], length / n, time / m));
        return this;
    }

    public ReactionDiffusionProblemBuilder grid(FieldConfiguration configuration) {
        problem = new ReactionDiffusionProblem(configuration);
        return this;
    }

    public ReactionDiffusionProblemBuilder system(Function... functions) {
        this.problem.setFunctions(functions);
        return this;
    }

    public ReactionDiffusionProblemBuilder initial(Function... conditions) {
        this.problem.setConditions(conditions);
        return this;
    }

    public ReactionDiffusionProblemBuilder diffusion(double... values) {
        this.problem.setDiffusion(values);
        return this;
    }

    public ReactionDiffusionProblemBuilder method(PartialDifferentialEquationSolver solver) {
        this.solver = solver;
        return this;
    }

    public ReactionDiffusionProblemBuilder calculate() {
        if (solver != null) {
            this.problem.calculate(solver);
        }
        return this;
    }

    public ReactionDiffusionProblemBuilder display() throws IOException {
        this.problem.display();
        return this;
    }

    public ReactionDiffusionProblemBuilder display(DataPrinter printer) throws IOException {
        this.problem.display(printer);
        return this;
    }

    public ReactionDiffusionProblemBuilder display(DataPrinter printer, int functionIndex) throws IOException {
        this.problem.display(printer, functionIndex);
        return this;
    }

    public ReactionDiffusionProblem build() {
        return problem;
    }

}
