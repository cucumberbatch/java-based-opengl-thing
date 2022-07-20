package view;

import problem.models.FieldConfiguration;

public class PreparedPrinterData {

    private double[][] data;


    public PreparedPrinterData(double[][] table) {
        this.data = table;
    }

    public PreparedPrinterData(FieldConfiguration configuration, int functionIndex) {
        this(configuration.getPrintableData(functionIndex));
    }

    public PreparedPrinterData(FieldConfiguration configuration) {
        this(configuration.getPrintableData());
    }

    public double[][] getData() {
        return data;
    }

}
