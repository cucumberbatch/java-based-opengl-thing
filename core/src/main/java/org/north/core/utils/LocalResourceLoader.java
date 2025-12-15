package org.north.core.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Iterator;

public class LocalResourceLoader implements ResourceLoader {
    private static final Logger log = LoggerFactory.getLogger(LocalResourceLoader.class);
    private final ClassLoader classLoader;

    public LocalResourceLoader() {
        classLoader = this.getClass().getClassLoader();
    }

    public LocalResourceLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public InputStream loadAsStream(String path) {
        return classLoader.getResourceAsStream(path);
    }

    @Override
    public String loadAsString(String path) throws IOException {
        try (InputStream resourceStream = loadAsStream(path)) {
            try (BufferedInputStream bufferedStream = new BufferedInputStream(resourceStream)) {
                log.info("Resource '{}' is loaded!", path);
                return new String(bufferedStream.readAllBytes());
            }
        }
    }

    @Override
    public URL getResourceUrl(String path) {
        return classLoader.getResource(path);
    }

    @Override
    public Iterator<URL> getAllResourceUrl(String path) throws IOException {
        return classLoader.getResources(path).asIterator();
    }

}
