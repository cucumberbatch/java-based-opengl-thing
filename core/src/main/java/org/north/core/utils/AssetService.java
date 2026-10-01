package org.north.core.utils;

import org.north.core.management.Asset;

public interface AssetService extends AutoCloseable {
    <T extends Asset> void registerAssetLoader(Class<T> type, AssetLoader<T> assetLoader);
    <T extends Asset> T get(String path, Class<T> type);
}
