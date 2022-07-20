package view;

import problem.Constants;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrinterDataFormatter {
    private List<StringBuilder> formattedData = new ArrayList<>();

    private DecimalFormat formatter;
    private char separationCharacter;
    private char newLineCharacter;

    public PrinterDataFormatter(String format, char separationCharacter, char newLineCharacter) {
        this.formatter = new DecimalFormat(format);
        this.separationCharacter = separationCharacter;
        this.newLineCharacter = newLineCharacter;
    }

    public String formatNumber(double element) {
        return Double.toString(element);
    }

    public String formatLine(double[] elements) {
        return Arrays.toString(elements);
    }


    public static class PrinterDataFormatterBuilder {
        private String format;
        private char separationCharacter;
        private char newLineCharacter;

        public PrinterDataFormatterBuilder setFormat(String format) {
            this.format = format;
            return this;
        }

        public PrinterDataFormatterBuilder setSeparationCharacter(char separationCharacter) {
            this.separationCharacter = separationCharacter;
            return this;
        }

        public PrinterDataFormatterBuilder setNewLineCharacter(char newLineCharacter) {
            this.newLineCharacter = newLineCharacter;
            return this;
        }

        public PrinterDataFormatter build() {
            return new PrinterDataFormatter(format, separationCharacter, newLineCharacter);
        }
    }
}
