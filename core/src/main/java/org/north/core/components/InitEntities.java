package org.north.core.components;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class InitEntities extends AbstractComponent {
    public InitEntities() {
        this.setState(ComponentState.READY_TO_INIT_STATE);
    }

    @Override
    protected void serialize(ObjectOutputStream out) throws IOException {

    }

    @Override
    protected void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {

    }
}
