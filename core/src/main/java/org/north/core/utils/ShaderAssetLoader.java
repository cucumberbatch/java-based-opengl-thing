package org.north.core.utils;

import org.north.core.graphics.Graphics;
import org.north.core.graphics.shader.Shader;

public class ShaderAssetLoader extends AssetLoader<Shader> {
    private final ResourceLoader resourceLoader;
    private final Graphics graphics;

    public ShaderAssetLoader(ResourceLoader resourceLoader, Graphics graphics) {
        this.resourceLoader = resourceLoader;
        this.graphics = graphics;
    }

    @Override
    protected Shader load(String assetPath, AssetService assetService) {
        return null;
    }

    @Override
    protected void unload(Shader shader) {

    }
}
