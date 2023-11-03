package ecs.systems.processes;

public interface InitProcess {

    /**
     * An interface method that allows the executions of individual instructions
     * of the component it inherits at the first frame of after the component
     * initialization
     *
     * @throws Exception
     * @author cucumberbatch
     */
    default void init() throws RuntimeException {};

}
