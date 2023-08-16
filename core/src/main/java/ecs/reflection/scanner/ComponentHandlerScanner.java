package ecs.reflection.scanner;

import ecs.components.Component;
import ecs.reflection.ComponentHandler;
import ecs.systems.System;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ComponentHandlerScanner {

    public class Pair<S extends System, C extends Component> {
        public S system;
        public Class<C> component;

        public Pair(S system, Class<C> component) {
            this.system = system;
            this.component = component;
        }
    }

    private ClassLoader classLoader;

    public ComponentHandlerScanner() {
        this.classLoader = ClassLoader.getSystemClassLoader();
    }

    public List<Pair<?, ?>> getAnnotatedClassesInPackage(String packageName)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
                   InstantiationException, IllegalAccessException, UnsupportedEncodingException {
        List<Pair<?, ?>> classes = new ArrayList<>();
        packageName = new String(packageName.getBytes(), StandardCharsets.UTF_8);
        URL packageUrl = classLoader.getResource(packageName);

        if (packageUrl != null) {
            String packagePath = URLDecoder.decode(packageUrl.getPath(), "UTF-8");
            File packageDir = new File(packagePath);
            if (packageDir.isDirectory()) {
                File[] files = packageDir.listFiles();
                for (File file : files) {
                    String className = file.getName();
                    if (className.endsWith(".class")) {
                        className = packageName.replace("/", ".") + "." + className.substring(0, className.length() - 6);
                        Class<?> loadedClass = classLoader.loadClass(className);
                        if (System.class.isAssignableFrom(loadedClass)) {
                            Class<? extends System> systemClass = loadedClass.asSubclass(System.class);
                            ComponentHandler componentHandlerAnnotation = loadedClass.getAnnotation(ComponentHandler.class);
                            if (componentHandlerAnnotation != null) {
                                Class<? extends Component> componentClass = componentHandlerAnnotation.value();
                                System<?> system = systemClass.getDeclaredConstructor().newInstance();
                                classes.add(new Pair<>(system, componentClass));
                            }
                        }
                    }
                }
            }
        }
        return classes;
    }
}
