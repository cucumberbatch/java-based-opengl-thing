package org.north.core.utils;

public class ResourceManager {
    private static ResourceManager instance;

    private static final String shadersPath = "core/src/main/resources/assets/shaders/";
    private static final String texturesPath = "core/src/main/resources/assets/textures/";

    private ResourceManager() {}

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    public int loadShader(String vertexShader, String fragmentShader) {
        return ShaderUtils.load(shadersPath + vertexShader, shadersPath + fragmentShader);
    }
}
