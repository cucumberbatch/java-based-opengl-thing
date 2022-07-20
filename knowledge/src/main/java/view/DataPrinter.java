package view;

import problem.models.FieldConfiguration;

import java.io.IOException;

public interface DataPrinter {
    void print(FieldConfiguration configuration) throws IOException;
}
