package numeric.integrals;

import math.types.Function;

public abstract class AbstractIntegral implements Integral {
    protected Function function;

    public AbstractIntegral(Function function) {
        this.function = function;
    }
}
