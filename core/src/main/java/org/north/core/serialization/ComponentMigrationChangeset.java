package org.north.core.serialization;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

public interface ComponentMigrationChangeset {
    void applyChangeset(UUID componentVersion,
                        ObjectInputStream in,
                        ObjectOutputStream out);
}
