package problem.models;

public class MultidimensionalMatrix {
    public int[]    dimensions;
    public double[] data;

    public MultidimensionalMatrix(int[] dimensions) {
        if (dimensions == null || dimensions.length == 0) {
            throw new IllegalArgumentException("Wrong dimensions in constructor");
        }

        int size = 1;

        for (int dimension : dimensions) {
            size *= dimension;
        }

        data = new double[size];
    }

    public MultidimensionalMatrix(int[] dimensions, double[] data) {
        if (dimensions == null || dimensions.length == 0) {
            throw new IllegalArgumentException("Wrong dimensions in constructor");
        }

        int size = 1;

        for (int dimension : dimensions) {
            size *= dimension;
        }

        if (data == null || data.length == size) {
            throw new IllegalArgumentException("Wrong dimensions in constructor");
        }

        this.dimensions = dimensions;
        this.data = data;
    }

    public double get(int i) {
        return data[i];
    }

    public double get(int i, int j) {
        return data[i + j * dimensions[0]];
    }

    public double get(int i, int j, int k) {
        return data[i + j * dimensions[0] + k * dimensions[1]];
    }

    public double get(int i, int j, int k, int l) {
        return data[i + j * dimensions[0] + k * dimensions[1] + l * dimensions[2]];
    }

    public double get(int... i) {
        int computedIndex = i[0];
        for (int index = 1; index < i.length; index++) {
            computedIndex += i[index] * dimensions[index - 1];
        }
        return data[computedIndex];
    }

    public void set(double value, int i) {
        data[i] = value;
    }

    public void set(double value, int i, int j) {
        data[i + j * dimensions[0]] = value;
    }

    public void set(double value, int i, int j, int k) {
        data[i + j * dimensions[0] + k * dimensions[1]] = value;
    }

    public void set(double value, int i, int j, int k, int l) {
        data[i + j * dimensions[0] + k * dimensions[1] + l * dimensions[2]] = value;
    }

    public void set(double value, int... i) {
        int computedIndex = i[0];
        for (int index = 1; index < i.length; index++) {
            computedIndex += i[index] * dimensions[index - 1];
        }
        data[computedIndex] = value;
    }

    public int getDimensionsDepth() {
        return dimensions.length;
    }

}
