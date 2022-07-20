package problem.models;

import problem.calculations.FiniteDifferenceMethod;
import problem.conditions.BoundaryCondition;
import view.DataPrinter;

import java.io.IOException;
import java.util.Objects;

import static problem.Constants.LOGGER;

public class Field {
    private FieldConfiguration configuration;

    public Field(FieldConfiguration configuration) {
        this.configuration = configuration;
    }

    public Field(double length, double time, int n, int m) {
        configuration = new FieldConfiguration(new double[m+1][n+1], length / n, time / m);
    }

    public void applyBoundaryCondition(BoundaryCondition condition) {
        LOGGER.info("Applying boundary conditions...");
        condition.apply(configuration);
    }

    public void applyDifferenceMethod(FiniteDifferenceMethod method) {
        LOGGER.info("Applying difference method...");
        method.solve(configuration);
    }

    public void viewDataOn(DataPrinter printer) throws IOException {
        LOGGER.info("Preparing data output...");
        printer.print(configuration);
    }

    public double[][] getDataTable() {
        return configuration.matrix[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return configuration.equals(field.configuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configuration);
    }
}
