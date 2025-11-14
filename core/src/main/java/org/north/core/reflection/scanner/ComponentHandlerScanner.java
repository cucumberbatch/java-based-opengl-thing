package org.north.core.reflection.scanner;

import org.north.core.component.Component;
import org.north.core.reflection.ComponentHandler;
import org.north.core.system.System;
import org.north.core.system.process.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ComponentHandlerScanner {

    public static class SystemComponentPair<S extends System<C>, C extends Component> {
        public Class<S> system;
        public Class<C> component;

        public SystemComponentPair(Class<S> system, Class<C> component) {
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

    private static final Logger log = LoggerFactory.getLogger(ComponentHandlerScanner.class);
    private final ClassLoader classLoader;

    public ComponentHandlerScanner() {
        this.classLoader = this.getClass().getClassLoader();
    }

    private List<Class<?>> loadClassesFromJar(URL jarUrl, String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        JarURLConnection jarConnection = (JarURLConnection) jarUrl.openConnection();
        JarFile jarFile = jarConnection.getJarFile();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String filePath = entry.getName();
            if (filePath.startsWith(packageName)) {
                loadAndAddIfClass(filePath, classes);
            }
        }
        return classes;
    }

    private List<Class<?>> loadClassesFromDir(URL packageUrl, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        String packagePath = URLDecoder.decode(packageUrl.getPath(), StandardCharsets.UTF_8);
        File packageDir = new File(packagePath);
        if (packageDir.isDirectory()) {
            File[] files = packageDir.listFiles();
            for (File file : Objects.requireNonNull(files)) {
                String filePath = packageName + "/" + file.getName();
                loadAndAddIfClass(filePath, classes);
            }
        }
        return classes;
    }

    private void loadAndAddIfClass(String filePath, List<Class<?>> classes) throws ClassNotFoundException {
        if (filePath.endsWith(".class") && !filePath.contains("$")) {
            String className = filePath
                    .replace('/', '.')
                    .substring(0, filePath.length() - 6);

            Class<?> loadedClass = classLoader.loadClass(className);
            classes.add(loadedClass);
        }
    }

    public List<Class<?>> loadAllClassesFromPackage(String packageName) throws ClassNotFoundException, FileNotFoundException, IOException {
        packageName = new String(packageName.getBytes(), StandardCharsets.UTF_8);
        URL packageUrl = classLoader.getResource(packageName);

        if (Objects.isNull(packageUrl)) {
            throw new FileNotFoundException("Package " + packageName + " not found!");
        }

        List<Class<?>> loadedClasses;
        if ("jar".equals(packageUrl.getProtocol())) {
            loadedClasses = loadClassesFromJar(packageUrl, packageName);
        } else {
            loadedClasses = loadClassesFromDir(packageUrl, packageName);
        }
        return loadedClasses;
    }

    public List<Class<? extends Process>> getAllProcessClasses(String processPackageName) throws ClassNotFoundException {
        List<Class<? extends Process>> processes = new ArrayList<>();
        try {
            for (Class<?> loadedClass : loadAllClassesFromPackage(processPackageName)) {
                if (!Process.class.equals(loadedClass) && Process.class.isAssignableFrom(loadedClass)) {
                    @SuppressWarnings("unchecked") Class<? extends Process> processClass = (Class<? extends Process>) loadedClass;
                    processes.add(processClass);
                }
            }
        } catch (IOException e) {
            return processes;
        }
        return processes;
    }

    public List<SystemComponentPair<?, ?>> getAnnotatedClassesInPackage(String packageName) throws ClassNotFoundException {
        List<SystemComponentPair<?, ?>> classes = new ArrayList<>();
        try {
            for (Class<?> loadedClass : loadAllClassesFromPackage(packageName)) {
                if (System.class.isAssignableFrom(loadedClass)) {
                    @SuppressWarnings("rawtypes") Class<? extends System> systemClass = loadedClass.asSubclass(System.class);

                    ComponentHandler componentHandlerAnnotation = loadedClass.getAnnotation(ComponentHandler.class);
                    if (Objects.isNull(componentHandlerAnnotation)) continue;
                    Class<? extends Component> componentClass = componentHandlerAnnotation.value();

                    @SuppressWarnings({"rawtypes", "unchecked"}) SystemComponentPair<?, ?> componentSystemPair = new SystemComponentPair<>(systemClass, componentClass);

                    classes.add(componentSystemPair);
                    // Logger.trace("Component system pair registered: " + componentSystemPair);
                }
            }
        } catch (IOException e) {
            return classes;
        }
        return classes;
    }
}
