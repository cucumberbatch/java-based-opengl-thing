package view.impl;

import problem.models.FieldConfiguration;
import problem.models.MultidimensionalMatrix;
import view.DataPrinter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class DATFileDataPrinter implements DataPrinter {
    private final String path;

    public DATFileDataPrinter(String path) {
        this.path = path;
    }

//    @Override
//    public void print(PrinterDataFormatter formatter) throws IOException {
//
//    }
//
//    @Override
//    public void print(PreparedPrinterData data) throws IOException {
//
//    }

    @Override
    public void print(FieldConfiguration configuration) throws IOException {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(path))) {
            MultidimensionalMatrix matrix = configuration.getMatrix();

            for (int dimension : matrix.dimensions) {
                stream.write(dimension);
            }
            for (double value : matrix.data) {
                stream.writeDouble(value);
            }
        }
    }

}
