package org.north.core.reflection.initializer;

import org.north.core.architecture.EntityManager;
import org.north.core.architecture.entity.ComponentManager;
import org.north.core.config.EngineConfig;
import org.north.core.systems.System;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassInitializer {
    private final EngineConfig config;

    public ClassInitializer(EngineConfig config) {
        this.config = config;
    }

    public <T extends System<?>> T initSystem(Class<T> systemClass)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

//        EntityManager em = config.getEntityManager();
//        ComponentManager cm = config.getComponentManager();
//        return systemClass.getDeclaredConstructor(EntityManager.class, ComponentManager.class).newInstance(em, cm);
        return null;
    }

    public <T> T instantiateObject(Class<T> objectClass)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

//        Constructor<?>[] declaredConstructors = objectClass.getDeclaredConstructors();
//        for (Constructor<?> constructor: declaredConstructors) {
//            constructor.get
//        }
        return null;
    }
}
