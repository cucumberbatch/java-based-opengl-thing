package org.north.core.reflection.scanner;

import org.north.core.components.Component;
import org.north.core.reflection.ComponentHandler;
import org.north.core.systems.System;
import org.north.core.utils.Logger;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ComponentHandlerScanner {

    public static class Pair<S extends System<C>, C extends Component> {
        public Class<S> system;
        public Class<C> component;

        public Pair(Class<S> system, Class<C> component) {
            this.system = system;
            this.component = component;
        }

        @Override
        public String toString() {
            return "Pair{" +
                    "system=" + system.getName() +
                    ", component=" + component.getName() +
                    '}';
        }
    }

    private final ClassLoader classLoader;

    public ComponentHandlerScanner() {
        this.classLoader = this.getClass().getClassLoader();
    }

    public List<Pair<?, ?>> getAnnotatedClassesInPackage(String packageName) throws ClassNotFoundException {
        List<Pair<?, ?>> classes = new ArrayList<>();
        packageName = new String(packageName.getBytes(), StandardCharsets.UTF_8);
        URL packageUrl = classLoader.getResource(packageName);

        if (Objects.isNull(packageUrl)) {
            // Logger.info("Package URL is null!");
            return classes;
        }

        String packagePath = URLDecoder.decode(packageUrl.getPath(), StandardCharsets.UTF_8);
        File packageDir = new File(packagePath);
        if (packageDir.isDirectory()) {
            // Logger.trace("Checking package classes in path: " + packageDir);
            File[] files = packageDir.listFiles();
            for (File file : Objects.requireNonNull(files)) {
                String className = file.getName();
                // Logger.trace("Found file: " + className);
                if (className.endsWith(".class")) {
                    className = packageName.replace("/", ".") + "." + className.substring(0, className.length() - 6);
                    Class<?> loadedClass = classLoader.loadClass(className);
                    if (System.class.isAssignableFrom(loadedClass)) {
                        @SuppressWarnings("rawtypes") Class<? extends System> systemClass = loadedClass.asSubclass(System.class);

                        ComponentHandler componentHandlerAnnotation = loadedClass.getAnnotation(ComponentHandler.class);
                        if (Objects.isNull(componentHandlerAnnotation)) continue;
                        Class<? extends Component> componentClass = componentHandlerAnnotation.value();

                        @SuppressWarnings("rawtypes") Pair componentSystemPair = new Pair<>(systemClass, componentClass);

                        classes.add(componentSystemPair);
                        // Logger.trace("Component system pair registered: " + componentSystemPair);
                    }
                }
            }
        }
        return classes;
    }
}
