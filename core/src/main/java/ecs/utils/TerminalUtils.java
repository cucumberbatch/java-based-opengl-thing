package ecs.utils;

import ecs.entities.Entity;
import matrices.Matrix4f;
import vectors.Vector3f;

import java.text.DecimalFormat;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class TerminalUtils {

    public static String ANSI_RESET   = "\u001b[0m";

    public static String ANSI_BLACK   = "\u001b[30m";
    public static String ANSI_RED     = "\u001b[31m";
    public static String ANSI_GREEN   = "\u001b[32m";
    public static String ANSI_YELLOW  = "\u001b[33m";
    public static String ANSI_BLUE    = "\u001b[34m";
    public static String ANSI_MAGENTA = "\u001b[35m";
    public static String ANSI_CYAN    = "\u001b[36m";
    public static String ANSI_WHITE   = "\u001b[37m";

    public static String ANSI_BRIGHT_BLACK   = "\u001b[30;1m";
    public static String ANSI_BRIGHT_RED     = "\u001b[31;1m";
    public static String ANSI_BRIGHT_GREEN   = "\u001b[32;1m";
    public static String ANSI_BRIGHT_YELLOW  = "\u001b[33;1m";
    public static String ANSI_BRIGHT_BLUE    = "\u001b[34;1m";
    public static String ANSI_BRIGHT_MAGENTA = "\u001b[35;1m";
    public static String ANSI_BRIGHT_CYAN    = "\u001b[36;1m";
    public static String ANSI_BRIGHT_WHITE   = "\u001b[37;1m";

    public static String ANSI_BACKGROUND_BLACK   = "\u001b[40m";
    public static String ANSI_BACKGROUND_RED     = "\u001b[41m";
    public static String ANSI_BACKGROUND_GREEN   = "\u001b[42m";
    public static String ANSI_BACKGROUND_YELLOW  = "\u001b[43m";
    public static String ANSI_BACKGROUND_BLUE    = "\u001b[44m";
    public static String ANSI_BACKGROUND_MAGENTA = "\u001b[45m";
    public static String ANSI_BACKGROUND_CYAN    = "\u001b[46m";
    public static String ANSI_BACKGROUND_WHITE   = "\u001b[47m";

    public static String ANSI_BACKGROUND_BRIGHT_BLACK   = "\u001b[40;1m";
    public static String ANSI_BACKGROUND_BRIGHT_RED     = "\u001b[41;1m";
    public static String ANSI_BACKGROUND_BRIGHT_GREEN   = "\u001b[42;1m";
    public static String ANSI_BACKGROUND_BRIGHT_YELLOW  = "\u001b[43;1m";
    public static String ANSI_BACKGROUND_BRIGHT_BLUE    = "\u001b[44;1m";
    public static String ANSI_BACKGROUND_BRIGHT_MAGENTA = "\u001b[45;1m";
    public static String ANSI_BACKGROUND_BRIGHT_CYAN    = "\u001b[46;1m";
    public static String ANSI_BACKGROUND_BRIGHT_WHITE   = "\u001b[47;1m";

    public static String ANSI_BOLD      = "\u001b[1m";
    public static String ANSI_UNDERLINE = "\u001b[4m";
    public static String ANSI_REVERSED  = "\u001b[7m";

    static final String  ansiCodeRegex        = "\\\\u\\S+m";
    static final Pattern ansiCodeRegexPattern = Pattern.compile(ansiCodeRegex);

    static final DecimalFormat decimalFormat = new DecimalFormat(" #0.00;-#");

    public static String deleteAnsiCodes(String text) {
        return ansiCodeRegexPattern.matcher(text).replaceAll("");
    }


    public static String indent4 = " -  ";

    private TerminalUtils() {}

    public static String formatOutputVector(Vector3f vector) {
        String start    = "[ ";
//        String end      = ANSI_RESET.concat("\t]");
//        String splitter = ANSI_RESET.concat(",\t");
        String end      = " ]";
        String newLine  = "\n";
        String splitter = ", ";
        StringBuffer formattedString = new StringBuffer();

//        formattedString
//                .append(start)
//                .append(ANSI_BRIGHT_RED).append(vector.x)
//                .append(splitter)
//                .append(ANSI_BRIGHT_GREEN).append(vector.y)
//                .append(splitter)
//                .append(ANSI_BRIGHT_BLUE).append(vector.z)
//                .append(end);
//
        formattedString
                .append(newLine)
                .append(start)
                .append(decimalFormat.format(vector.x))
                .append(splitter)
                .append(decimalFormat.format(vector.y))
                .append(splitter)
                .append(decimalFormat.format(vector.z))
                .append(end);

        return formattedString.toString();
//        return vector.toString();
    }

    public static String formatOutputMatrix(Matrix4f matrix) {
/*
        String matrixOutputFormatString = "[$%10.3f, $%10.3f, $%10.3f, $%10.3f]\n[$%10.3f, $%10.3f, $%10.3f, $%10.3f]\n[$%10.3f, $%10.3f, $%10.3f, $%10.3f]\n[$%10.3f, $%10.3f, $%10.3f, $%10.3f]\n";

        return String.format(matrixOutputFormatString, matrix.elements matrix[0], matrix[1], matrix[2], matrix[3], matrix[4], matrix[5], matrix[6], matrix[7], matrix[8], matrix[9], matrix[10], matrix[11], matrix[12], matrix[13], matrix[14], matrix[15]);

    }
*/

        String start    = "║ ";
//        String end      = ANSI_RESET.concat("\t]");
//        String newLine  = ANSI_RESET.concat("\n");
//        String splitter = ANSI_RESET.concat(",\t");
        String end      = " ║";
        String newLine  = "\n";
        String splitter = ", ";
        StringBuffer formattedString = new StringBuffer("\n");


        for (int i = 0; i < 4; i++) {
            formattedString.append(start);
            for (int j = 0; j < 4; j++) {
                formattedString.append(decimalFormat.format(matrix.elements[i + j * 4]));
                if (j < 3) {
                    formattedString.append(splitter);
                }
            }
            formattedString.append(end);
            if (i < 3) {
                formattedString.append(newLine);
            }
        }
        return formattedString.toString();
    }

    public static String replaceWithIndents(String string, int indentLevel) {
        String indent = "  - ";
        StringBuffer accumulator = new StringBuffer();

        IntStream
                .range(0, indentLevel)
                .mapToObj(i -> indent)
                .forEach(accumulator::append);

        return string.replace("\n", accumulator);
    }

//    public static String formatOutputComponent(ECSComponent component) {
//
//    }

    public static String formatOutputEntity(Entity entity, int level) {
//        String indent   = " -  ";
//        String start    = " + ";
//        String end      = ANSI_RESET.concat("");
//        StringBuffer formattedString = new StringBuffer();
//        boolean hasName = entity.name != null && !entity.name.isEmpty();
//
//        formattedString.append(start);
//
//        if (hasName) {
//            formattedString
//                    .append("(")
//                    .append(ANSI_BOLD)
//                    .append(entity.name)
//                    .append(ANSI_RESET)
//                    .append(") ");
//        }
//
//        formattedString
//                .append(ANSI_BRIGHT_WHITE)
//                .append("\033[38;5;255;48;5;73m")
//                .append(entity.id)
//                .append(ANSI_RESET)
//                .append(end);
//
//        return formattedString.toString();
        return String.format("%s(%s)%s", 
            ANSI_BACKGROUND_BRIGHT_WHITE + ANSI_BLACK,
            entity.name,
            ANSI_RESET)/*"(" + entity.name + ")"*/;

    }
}