package problem.solution;

import math.types.Function;
import math.series.Fourier;
import math.series.computing.CosineFourierCore;
import problem.models.FieldConfiguration;
import problem.models.MultidimensionalMatrix;
import problem.models.ReactionDiffusionProblem;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Math.*;
import static problem.Constants.LOGGER;

public class ReactionDiffusionPartialDifferentialEquationSolver implements PartialDifferentialEquationSolver {
    private int  fourierCoefficientsNumber = 5;

    private long startMeasurementTime;
    private long previousDeltaTime;
    private long[] deltaTimeBuffer = new long[3];
    private int timeMeasurementIndex = 0;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ReactionDiffusionPartialDifferentialEquationSolver(int fourierCoefficientsNumber) {
        this.fourierCoefficientsNumber = fourierCoefficientsNumber;
    }

    public ReactionDiffusionPartialDifferentialEquationSolver(int targetDistribution, int fourierCoefficientsNumber) {
        this(fourierCoefficientsNumber);
    }

    @Override
    public void solve(ReactionDiffusionProblem problem) {
        FieldConfiguration configuration = problem.getConfiguration();
        Fourier     fourier    = new Fourier(new CosineFourierCore(), fourierCoefficientsNumber);
        Function[]  functions  = problem.getFunctions();
        Function[]  conditions = problem.getConditions();
        double[]    diffusion  = problem.getDiffusion();

        // массив для значений функций, зависящих от распределения на предыдущем слое
        double[] temporaryFunction      = new double[configuration.n];
        double[] temporaryDistribution  = new double[configuration.n];

        // массив значений функции распределения
        configuration.matrix = new double[configuration.m][configuration.n][functions.length];
        double[][][] u  = configuration.matrix;

        MultidimensionalMatrix impMatrix = configuration.improvedMatrix;

        // массив коэффициентов фурье
        double[][][] a  = new double[configuration.m][functions.length][fourier.amount()];

        // временный массив коэффициентов фурье для дальнейших преобразований
        double[][]   fi = new double[functions.length][fourier.amount()];

        int layerIndex;         // индекс слоя
        int lengthIndex;        // индекс элемента длины слоя
        int functionIndex;      // индекс уравнения системы
        int coefficientIndex;   // индекс коэффицинтов Фурье

        // засечь время начала расчета
        startMeasurementTime = previousDeltaTime = System.currentTimeMillis();

        LOGGER.info(String.format("calculating 0 out of %d...", configuration.m));

        // итерирование по всем функциям распределения при начальных условиях
        for (functionIndex = 0; functionIndex < functions.length; functionIndex++) {

            // итерирование по всей длине слоя
            for (lengthIndex = 0; lengthIndex < configuration.n; lengthIndex++) {

                // дискретизация начальных условий, заданных в аналитическом виде
                temporaryDistribution[lengthIndex] = conditions[functionIndex].value(lengthIndex * configuration.lengthStep);
//                u[0][lengthIndex][functionIndex] = temporaryDistribution[lengthIndex];
                impMatrix.set(temporaryDistribution[lengthIndex], 0, lengthIndex, functionIndex);
            }

            // разложение начального распределения на коэффициенты Фурье
            a[0][functionIndex] = fourier.transform(temporaryDistribution, 0, configuration.length);
        }

        // итерирование по остальным слоям
        for (layerIndex = 1; layerIndex < configuration.m; layerIndex++) {

            // расчет оставшегося времени каждые 10 слоев
            if (layerIndex % 10 == 0) {

                long currentTime = System.currentTimeMillis();

                deltaTimeBuffer[timeMeasurementIndex % deltaTimeBuffer.length] = currentTime - previousDeltaTime;
                long meanOfThreeDeltas = (deltaTimeBuffer[0] + deltaTimeBuffer[1] + deltaTimeBuffer[2]) / 3;
                long estimatedEndTime = (configuration.m * meanOfThreeDeltas / 10) + startMeasurementTime;

                previousDeltaTime = System.currentTimeMillis();
                timeMeasurementIndex++;

                LOGGER.info(String.format("[%d out of %d] ", layerIndex, configuration.m, estimatedEndTime) + "will end at [" + dateFormat.format(new Date(estimatedEndTime)) + " ]");
            }

            // итерироваие по функциям распределения
            for (functionIndex = 0; functionIndex < functions.length; functionIndex++) {

                // итерирование по всей длине поля
                for (lengthIndex = 0; lengthIndex < configuration.n; lengthIndex++) {

                    // нахождение значений данной функции при значениях распределения с предыдущего слоя
                    temporaryFunction[lengthIndex] = functions[functionIndex].value(0, u[layerIndex - 1][lengthIndex]);
                }

                // нахождение Фурье образа для каждого распределения
                fi[functionIndex] = fourier.transform(temporaryFunction, 0, configuration.length);

                // вынесение некоторого множителя "за скобки" для уменьшения вычислительной нагрузки
                double factor = configuration.timeStep * diffusion[functionIndex] * PI * PI / configuration.length / configuration.length;

                // решение системы в преобразованном виде
                // нахождение коэффициентов Фурье для функций распределения
                for (coefficientIndex = 0; coefficientIndex < fourier.amount(); coefficientIndex++) {
                    a[layerIndex][functionIndex][coefficientIndex] =
                            (1 / (factor * coefficientIndex * coefficientIndex + 1))
                            * (a[layerIndex - 1][functionIndex][coefficientIndex]
                            + configuration.timeStep * fi[functionIndex][coefficientIndex]);
                }

                // нахождение значений функции распределения через обратное преобразование Фурье
                temporaryDistribution = fourier.inverseTransform(a[layerIndex][functionIndex], configuration.n, configuration.lengthStep);

                for (lengthIndex = 0; lengthIndex < configuration.n; lengthIndex++) {
                    u[layerIndex][lengthIndex][functionIndex] = temporaryDistribution[lengthIndex];
                }
            }
        }
    }
}
