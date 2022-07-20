package problem.models;

import problem.calculations.FiniteDifferenceMethod;
import problem.conditions.BoundaryCondition;
import view.DataPrinter;

import java.io.IOException;

public class FieldManager {
    private Field field;


    public FieldManager init(FieldConfiguration configuration) {
        this.field = new Field(configuration);
        return this;
    }

    public FieldManager init(double length, double time,int n, int m) {
        this.field = new Field(length, time, n, m);
        return this;
    }

    public FieldManager applyBoundaryCondition(BoundaryCondition condition) {
        field.applyBoundaryCondition(condition);
        return this;
    }

    public FieldManager applyDifferenceMethod(FiniteDifferenceMethod method) {
        field.applyDifferenceMethod(method);
        return this;
    }

    public FieldManager viewDataOn(DataPrinter printer) throws IOException {
        field.viewDataOn(printer);
        return this;
    }

    public Field done() {
        return field;
    }
}
