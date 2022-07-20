package view.impl;

import problem.models.FieldConfiguration;
import problem.models.MultidimensionalMatrix;
import view.DataPrinter;
import view.PrinterDataFormatter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class CSVFileDataPrinter implements DataPrinter {
    private final PrinterDataFormatter formatter;
    private final String path;


    public CSVFileDataPrinter(PrinterDataFormatter formatter, String path) {
        this.formatter = formatter;
        this.path = path;
    }

    @Override
    public void print(FieldConfiguration configuration) throws IOException {
        try (Writer writer = new FileWriter(path)) {
            MultidimensionalMatrix matrix = configuration.getMatrix();

            writeDataIntoFile(configuration, writer);

        }
    }

    private void writeDataIntoFile(FieldConfiguration configuration, Writer writer) {
        writeDataIntoFileRecursive(configuration, writer, 0);
    }

    private void writeDataIntoFileRecursive(FieldConfiguration configuration, Writer writer, int level) {

    }

}
