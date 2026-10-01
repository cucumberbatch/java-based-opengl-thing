package org.north.core.graphics;

import org.north.core.io.LocalResourceLoader;
import org.north.core.io.ResourceLoader;

/**
 * @deprecated this class will be removed soon because of creating new more OOP approach of working with
 * assets/resources via AssetLoader interface with different implementations based on asset nature
 */
@Deprecated
public class ShaderLoader {
    private static ShaderLoader instance;
    private static final String shadersPath = "assets/shaders/";
    private static final String texturesPath = "assets/textures/";

    private final ResourceLoader resourceLoader;

    private ShaderLoader() {
        resourceLoader = new LocalResourceLoader();
    }

    public static ShaderLoader getInstance() {
        if (instance == null) {
            instance = new ShaderLoader();
        }
        return instance;
    }

    @Deprecated
    public int loadShader(String vertexShader, String fragmentShader) {
        return ShaderUtils.load(shadersPath + vertexShader, shadersPath + fragmentShader);
    }
}
