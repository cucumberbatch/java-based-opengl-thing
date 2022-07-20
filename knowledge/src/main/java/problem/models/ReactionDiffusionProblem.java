package problem.models;

import java.io.IOException;

import math.types.Function;
import problem.solution.PartialDifferentialEquationSolver;
import view.DataPrinter;
import view.impl.ConsoleDataPrinter;

public class ReactionDiffusionProblem {
    private FieldConfiguration  configuration;
    private Function[]          functions;
    private Function[]          conditions;
    private double[]            diffusion;


    public ReactionDiffusionProblem(FieldConfiguration configuration, Function[] functions, Function[] conditions, double[] diffusion) {
        this(configuration);
        this.setFunctions(functions);
        this.setConditions(conditions);
        this.setDiffusion(diffusion);
    }

    public ReactionDiffusionProblem(FieldConfiguration configuration) {
        this.configuration = configuration;
    }

    public ReactionDiffusionProblem(int x, double dx, int t, double dt) {
        this(new FieldConfiguration(new double[t+1][x+1], dx, dt));
    }

    public Function[] getFunctions() {
        return functions;
    }

    public void setFunctions(Function[] functions) {
        if (functions == null) return;
        this.functions = functions;
    }

    public Function[] getConditions() {
        return conditions;
    }

    public FieldConfiguration getConfiguration() {
        return configuration;
    }

    public double[] getDiffusion() {
        return diffusion;
    }

    public void setDiffusion(double[] diffusion) {
        this.diffusion = diffusion;
    }

    public void setConditions(Function[] conditions) {
        if (conditions == null) return;
        this.conditions = conditions;
    }

    public int getFunctionsNumber() {
        return functions.length;
    }

    public void display() throws IOException {
        this.display(new ConsoleDataPrinter());
    }

    public void display(DataPrinter printer) throws IOException {
        printer.print(configuration);
    }

    public void display(DataPrinter printer, int functionIndex) throws IOException {
        printer.print(configuration);
    }

    // TODO: needs to check for the correct working
    public void calculate(PartialDifferentialEquationSolver solver) {
        solver.solve(this);
    }

}
