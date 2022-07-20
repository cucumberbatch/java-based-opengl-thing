package problem;

import problem.models.Field;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Constants {

    public static Logger LOGGER;

    //Logger initialization by property file
    static {
        InputStream stream = Field.class.getClassLoader().
                getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
            LOGGER = Logger.getLogger(Field.class.getName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Parameters of grid:
    public static final double Length	= 1.0d;     // length or depth of sample
    public static final double Time		= 0.1d;     // total time of experiment
    public static final int    N		= 10;       // amount of steps for length fracture
    public static final int    M		= 20;       // amount of steps for time fracture

    // Simulation parameters
    public static final double U0 		= 1.0d;
    public static final double lambda 	= 1.0d;
    public static final double rho 		= 1.0d;
    public static final double c		= 1.0d;
    public static final double alpha 	= 1.0d / 2.0d;
    public static final double a_sqr	= lambda / (rho * c);
    public static final double p0		= 48;
    public static final double p		= p0 / (rho * c);
    public static final double b0		= 4 * a_sqr * (3 - alpha) / (1 - alpha) / (1 - alpha);
    public static final double b1		= 2 * Math.pow(U0, alpha - 1) * p * (3 - alpha) / (1 + alpha);
    public static final double L		= Math.sqrt(b0 / b1);

    // Output data folder destination
    public static final String DATA_FOLDER = "D:\\data\\dat\\";

    // Output format for data in console
    public static final String CONSOLE_OUTPUT_FORMAT = "#0.00";

    public static final char   CSV_SEMICOLON_SEPARATION_CHARACTER  = ';';
    public static final char   CSV_TABULATION_SEPARATION_CHARACTER = '\t';

    public static final char NEW_LINE_CHARACTER = '\n';
    public static final char NULL_CHARACTER     = '\0';


    public enum LogMessage {
        // Difference method messages
        DIFF_METHOD_INACCURACY("A high probability risk of calculation error!"),
        DIFF_METHOD_DONE("Difference method applied!"),
        DIFF_METHOD_CALCULATING("Calculating difference..."),

        // Data viewers messages
        DATA_SERIALIZER_DONE("Serialization data process is complete!"),

        // Program complete message
        DONE("Calculations complete!");



        private String messageString;

        LogMessage(String messageString) {
            this.messageString = messageString;
        }

        public String getMessageString() {
            return messageString;
        }

    }
}
