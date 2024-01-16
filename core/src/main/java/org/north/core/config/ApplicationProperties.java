package org.north.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class ApplicationProperties {
    private static ApplicationProperties instance;
    private final Properties properties;

    private ApplicationProperties() throws IOException {
        InputStream resourceStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");
        this.properties = new Properties();
        this.properties.load(resourceStream);
    }

    public static String getProperty(String property) {
        if (Objects.isNull(instance)) {
            try {
                instance = new ApplicationProperties();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return instance.properties.getProperty(property);
    }
}
