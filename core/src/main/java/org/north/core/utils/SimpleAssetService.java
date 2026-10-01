package org.north.core.utils;

import org.north.core.graphics.Graphics;
import org.north.core.graphics.Texture;
import org.north.core.management.Asset;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleAssetService implements AssetService {
    private final Map<Class<?>, AssetLoader<?>> loaderRegistry = new ConcurrentHashMap<>();

    public SimpleAssetService(Graphics graphics) {
        loaderRegistry.put(Texture.class, new TextureAssetLoader(graphics));
    }

    @Override
    public <T extends Asset> void registerAssetLoader(Class<T> type, AssetLoader<T> assetLoader) {
        loaderRegistry.put(type, assetLoader);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Asset> T get(String path, Class<T> type) {
        return ((AssetLoader<T>) loaderRegistry.get(type)).get(path, this);
    }

    @Override
    public void close() {
        for (AssetLoader<?> assetLoader : loaderRegistry.values()) {
            assetLoader.close();
        }
        loaderRegistry.clear();
    }
}

