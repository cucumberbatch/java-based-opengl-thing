package org.north.core.utils;

import org.north.core.graphics.shader.Shader;

import java.util.HashMap;
import java.util.Map;

//todo: add Graphics dependency to work with shader compilation/linking logic
public class ShaderManager implements ResourceManager<Shader, Integer> {
    private final ResourceLoader resourceLoader;
    private final Map<Integer, Integer> shaderHashToIdMap;
    private final String basePath;

    public ShaderManager(ResourceLoader resourceLoader, String basePath) {
        this.resourceLoader = resourceLoader;
        this.shaderHashToIdMap = new HashMap<>();
        this.basePath = basePath;
    }

    @Override
    public Shader getResource(String path) {
        return null;
    }

    @Override
    public Integer getResourceId(String path) {
        return 0;
    }

}
