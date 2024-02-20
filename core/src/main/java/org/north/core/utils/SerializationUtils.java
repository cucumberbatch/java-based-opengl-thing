package org.north.core.utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

public class SerializationUtils {
    public static UUID readUUID(ObjectInputStream in) throws IOException {
        return new UUID(in.readLong(), in.readLong());
    }

    public static void writeUUID(ObjectOutputStream out, UUID uuid) throws IOException {
        out.writeLong(uuid.getMostSignificantBits());
        out.writeLong(uuid.getLeastSignificantBits());
    }
}
