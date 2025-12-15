package org.north.core.utils;

/**
 * @deprecated this class will be removed soon because of creating new more OOP approach of working with
 * assets/resources via ResourceManager interface with different implementations based on asset nature
 */
@Deprecated
public class OldResourceManager {
    private static OldResourceManager instance;
    private static final String shadersPath = "assets/shaders/";
    private static final String texturesPath = "assets/textures/";

    private final ResourceLoader resourceLoader;

    private OldResourceManager() {
        resourceLoader = new LocalResourceLoader();
    }

    public static OldResourceManager getInstance() {
        if (instance == null) {
            instance = new OldResourceManager();
        }
        return instance;
    }

    public int loadShader(String vertexShader, String fragmentShader) {
        return ShaderUtils.load(shadersPath + vertexShader, shadersPath + fragmentShader);
    }
}
