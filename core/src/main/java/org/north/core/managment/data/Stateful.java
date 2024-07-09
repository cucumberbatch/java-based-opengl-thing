package org.north.core.managment.data;

public interface Stateful<State extends Enum<State>> {

    State getState();

    void setState(State state);

    default boolean inState(State state) {
        return getState().equals(state);
    }

}
