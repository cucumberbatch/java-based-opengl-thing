package org.north.core.reflection.initializer;

import org.north.core.systems.System;

import java.lang.reflect.InvocationTargetException;

public class ClassInitializer {

    public <T extends System<?>> T initSystem(Class<T> systemClass)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return systemClass.getDeclaredConstructor().newInstance();
    }
}
