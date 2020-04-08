package ecs.util;

public enum Layer {
    GUI(10),
    DEFAULT(100),
    INNER(200);

    private int index;

    Layer(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
