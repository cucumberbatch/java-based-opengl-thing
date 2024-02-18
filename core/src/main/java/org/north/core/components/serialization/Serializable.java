package org.north.core.components.serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

public interface Serializable<O extends OutputStream, I extends InputStream> {
    void serializeObject(O outputStream) throws IOException;
    void deserializeObject(I inputStream) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
